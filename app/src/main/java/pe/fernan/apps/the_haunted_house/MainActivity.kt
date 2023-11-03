package pe.fernan.apps.the_haunted_house

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pe.fernan.apps.the_haunted_house.ui.theme.TheHauntedHouseTheme
import kotlin.random.Random

/*
 * Este es un reto especial por Halloween.
 * Te encuentras explorando una mansión abandonada llena de habitaciones.
 * En cada habitación tendrás que resolver un acertijo para poder avanzar a la siguiente.
 * Tu misión es encontrar la habitación de los dulces.
 *
 * Se trata de implementar un juego interactivo de preguntas y respuestas por terminal.
 * (Tienes total libertad para ser creativo con los textos)
 *
 * - 🏰 Casa: La mansión se corresponde con una estructura cuadrada 4 x 4
 *   que deberás modelar. Las habitaciones de puerta y dulces no tienen enigma.
 *   (16 habitaciones, siendo una de entrada y otra donde están los dulces)
 *   Esta podría ser una representación:
 *   🚪⬜️⬜️⬜️
 *   ⬜️👻⬜️⬜️
 *   ⬜️⬜️⬜️👻
 *   ⬜️⬜️🍭⬜️
 * - ❓ Enigmas: Cada habitación propone un enigma aleatorio que deberás responder con texto.
 *   Si no lo aciertas no podrás desplazarte.
 * - 🧭 Movimiento: Si resuelves el enigma se te preguntará a donde quieres desplazarte.
 *   (Ejemplo: norte/sur/este/oeste. Sólo deben proporcionarse las opciones posibles)
 * - 🍭 Salida: Sales de la casa si encuentras la habitación de los dulces.
 * - 👻 (Bonus) Fantasmas: Existe un 10% de que en una habitación aparezca un fantasma y
 *   tengas que responder dos preguntas para salir de ella.
 */


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TheHauntedHouseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    Rooms(Modifier.fillMaxSize())

                    //Greeting("Android")
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return super.onKeyDown(keyCode, event)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Rooms2(modifier: Modifier) {
    val requester = remember { FocusRequester() }

    val items = (1..16).toList()
    LazyVerticalGrid(
        modifier = modifier
            .onKeyEvent { event ->
                if (event.type == KeyEventType.KeyDown) {
                    when (event.key) {
                        Key.DirectionRight -> Log.d("Keys", "DirectionRight")
                        Key.DirectionLeft -> Log.d("Keys", "DirectionLeft")

                        Key.DirectionCenter -> Log.d("Keys", "DirectionCenter")

                        Key.DirectionUp -> Log.d("Keys", "DirectionUp")
                        Key.DirectionDown -> Log.d("Keys", "DirectionDown")

                    }
                    true // Consumir el evento
                } else {
                    false // Deja que otros procesen el evento
                }

            }
            .focusRequester(requester)
            .focusable(), columns = GridCells.Fixed(4)
    ) {
        items(items) {
            val randomColor = Color(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(2.dp),
                colors = CardDefaults.cardColors(containerColor = randomColor)

            ) {
                Text(text = it.toString())
            }
        }
    }
}

val LightGreen = Color(0xFF31DE84)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Rooms(modifier: Modifier) {
    val requester = remember { FocusRequester() }

    val items = (1..16).toList()
    var focusedIndex by remember { mutableStateOf(0) }

    LazyVerticalGrid(
        modifier = modifier
            .background(Color.Gray)
            .padding(20.dp)
            .onKeyEvent { event ->
                if (event.type == KeyEventType.KeyDown) {
                    when (event.key) {
                        Key.DirectionRight -> {
                            if (focusedIndex < items.size - 1) {
                                focusedIndex++
                            }
                        }

                        Key.DirectionLeft -> {
                            if (focusedIndex > 0) {
                                focusedIndex--
                            }
                        }

                        Key.DirectionCenter -> Log.d("Keys", "DirectionCenter")
                        Key.DirectionUp -> {
                            if (focusedIndex >= 4) {
                                focusedIndex -= 4
                            }
                        }

                        Key.DirectionDown -> {
                            if (focusedIndex + 4 < items.size) {
                                focusedIndex += 4
                            }
                        }
                    }
                    true
                } else {
                    false
                }
            }
            .focusRequester(requester)
            .focusable(), columns = GridCells.Fixed(4)
    ) {
        items(items) {

            val icon = if (items.indexOf(it) == focusedIndex) {
                R.drawable.ic_android
            } else {
                null
            }

            val randomColor = if (items.indexOf(it) == focusedIndex) {
                Color.Red
            } else {
                Color.White
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(2.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                //Text(text = it.toString())
                if(icon != null){
                    Icon(
                        modifier = Modifier.fillMaxSize().padding(15.dp),
                        painter = painterResource(id = icon),
                        contentDescription = "icon",
                        tint = LightGreen
                    )
                }
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TheHauntedHouseTheme {
        Greeting("Android")
    }
}