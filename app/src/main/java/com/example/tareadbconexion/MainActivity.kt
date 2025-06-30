package com.example.tareadbconexion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tareadbconexion.ui.theme.TareaDbConexionTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            MostrarAlumno()
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MostrarAlumno(){
    var msj  by remember { mutableStateOf(false) }
    var alumnojson by remember { mutableStateOf("") }

    val db = FirebaseFirestore.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {

            db.collection("alumnos")
                .limit(1)
                .get()
                .addOnSuccessListener { result ->
                    if(!result.isEmpty){
                        val alumno = result.documents[0].toObject(Alumno::class.java)
                   alumnojson = Gson().toJson(alumno)
                        msj = true

                    }else{
                        alumnojson = "No se encontro ningun alumno"
                        msj = true

                    }
                }
                .addOnFailureListener {
                    alumnojson = "Error al leer Firestore"
                    msj = true
                }
        }) {
            Text("Cargar alumno desde Firebase ")
        }

        if (msj) {
           AlertDialog(
               onDismissRequest = {msj = false},
               title = {Text("Alumno desde firebase ")},
               text = {Text(alumnojson)},
               confirmButton = {
                   TextButton(onClick = {msj = false}) {
                       Text("cerrar")
                   }
               }
           )
        }

    }


}


