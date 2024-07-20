package com.example.database

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.database.ui.theme.DatabaseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DatabaseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DatabaseApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatabaseApp() {
    var showDialog by remember { mutableStateOf(false) }
    var employees by remember { mutableStateOf(listOf<Employee>()) }
    var ename by remember { mutableStateOf("") }
    var eaddress by remember { mutableStateOf("") }
    var egender by remember { mutableStateOf("") }
    var edob by remember { mutableStateOf("") }
    var erole by remember { mutableStateOf("") }
    var editMode by remember { mutableStateOf(false) }
    var employeeToEdit by remember { mutableStateOf<Employee?>(null) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text("Employee Database".uppercase())
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            showDialog = true
            editMode = false
        }) {
            Text(text = "ADD")
        }

        LazyColumn {
            items(employees) { emp ->
                EmployeeRow(emp, onEdit = {
                    ename = it.name
                    eaddress = it.Address
                    egender = it.Gender
                    edob = it.DOB
                    erole = it.Role
                    employeeToEdit = it
                    editMode = true
                    showDialog = true
                }, onDelete = {
                    employees = employees.filter { e -> e.id != it.id }
                })
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = {
                        if (ename.isNotBlank() && edob.isNotBlank() && egender.isNotBlank() && eaddress.isNotBlank() && erole.isNotBlank()) {
                            if (editMode && employeeToEdit != null) {
                                employees = employees.map {
                                    if (it.id == employeeToEdit!!.id) {
                                        it.copy(name = ename, Address = eaddress, Gender = egender, DOB = edob, Role = erole)
                                    } else {
                                        it
                                    }
                                }
                            } else {
                                val emp = Employee(
                                    id = employees.size + 1,
                                    name = ename,
                                    DOB = edob,
                                    Address = eaddress,
                                    Gender = egender,
                                    Role = erole
                                )
                                employees = employees + emp
                            }
                            showDialog = false
                            ename = ""
                            egender = ""
                            eaddress = ""
                            edob = ""
                            erole = ""
                            editMode = false
                            employeeToEdit = null
                        }
                    }) {
                        Text(text = if (editMode) "UPDATE" else "SAVE")
                    }
                    Button(onClick = { showDialog = false }) {
                        Text(text = "CANCEL")
                    }
                }
            },
            title = { Text(if (editMode) "Edit Employee" else "Add Employee") },
            text = {
                Column {
                    OutlinedTextField(
                        value = ename,
                        onValueChange = { ename = it },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        label = { Text("Enter name") }
                    )
                    OutlinedTextField(
                        value = eaddress,
                        onValueChange = { eaddress = it },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        label = { Text("Enter Address") }
                    )
                    OutlinedTextField(
                        value = edob,
                        onValueChange = { edob = it },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        label = { Text("Enter DOB (DD-MM-YYYY)") }
                    )
                    OutlinedTextField(
                        value = egender,
                        onValueChange = { egender = it },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        label = { Text("Enter Gender") }
                    )
                    OutlinedTextField(
                        value = erole,
                        onValueChange = { erole = it },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        label = { Text("Enter Role") }
                    )
                }
            }
        )
    }
}

@Composable
fun EmployeeRow(emp: Employee, onEdit: (Employee) -> Unit, onDelete: (Employee) -> Unit) {
    var showDetailsDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .border(1.dp, Color.Black)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(10.dp)
        ) {
            Text("${emp.name}\n${emp.Role}")
        }
        IconButton(onClick = { showDetailsDialog = true }) {
            Icon(imageVector = Icons.Default.AccountBox, contentDescription = "Details")
        }
        IconButton(onClick = { onEdit(emp) }) {
            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
        }
        IconButton(onClick = { onDelete(emp) }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
        }
    }

    if (showDetailsDialog) {
        AlertDialog(
            onDismissRequest = { showDetailsDialog = false },
            confirmButton = {
                Button(onClick = { showDetailsDialog = false }) {
                    Text("Close")
                }
            },
            title = { Text("Employee Details") },
            text = {
                Column {
                    Text("Name: ${emp.name} ,   ${emp.Gender}")
                    Text("ID: ${emp.id}  , DOB: ${emp.DOB}")
                    Text("Role: ${emp.Role}")
                    Text("Address: ${emp.Address}")
                }
            }
        )
    }
}

data class Employee(
    val id: Int,
    val name: String,
    val Gender: String,
    val DOB: String,
    val Address: String,
    val Role: String
)
