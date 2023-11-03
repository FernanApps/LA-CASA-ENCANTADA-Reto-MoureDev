package pe.fernan.apps.the_haunted_house

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
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

    val flipRandom = items.random()

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
                if (icon != null) {

                    if (it == flipRandom) {
                        var currentCardFace by remember { mutableStateOf(CardFace.Front) }
                        var isFlipped by remember { mutableStateOf(false) }

                        FlipCardImp(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .padding(2.dp),
                            currentCardFace = currentCardFace
                            , back = {
                                Image(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(10.dp),
                                    painter = painterResource(id = R.drawable.ic_ghost),
                                    contentDescription = "icon_ghost",
                                )
                            }
                        )


                        LaunchedEffect(key1 = currentCardFace){
                            if(!isFlipped){
                                delay(500)
                                currentCardFace = currentCardFace.next
                                isFlipped = true

                            }

                        }



                    } else {
                        Icon(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(15.dp),
                            painter = painterResource(id = icon),
                            contentDescription = "icon",
                            tint = LightGreen
                        )
                    }




                }
            }


        }
    }
}


// https://fvilarino.medium.com/creating-a-rotating-card-in-jetpack-compose-ba94c7dd76fb

enum class CardFace(val angle: Float) {
    Front(0f) {
        override val next: CardFace
            get() = Back
    },
    Back(180f) {
        override val next: CardFace
            get() = Front
    };

    abstract val next: CardFace
}

enum class RotationAxis {
    AxisX,
    AxisY,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlipCard(
    cardFace: CardFace,
    onClick: (CardFace) -> Unit,
    modifier: Modifier = Modifier,
    axis: RotationAxis = RotationAxis.AxisY,
    back: @Composable () -> Unit = {},
    front: @Composable () -> Unit = {},
) {
    val rotation = animateFloatAsState(
        targetValue = cardFace.angle,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing,
        )
    )
    Card(
        onClick = { onClick(cardFace) },
        modifier = modifier
            .graphicsLayer {
                if (axis == RotationAxis.AxisX) {
                    rotationX = rotation.value
                } else {
                    rotationY = rotation.value
                }
                cameraDistance = 12f * density
            },
    ) {
        if (rotation.value <= 90f) {
            Box(
                Modifier.fillMaxSize()
            ) {
                front()
            }
        } else {
            Box(
                Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        if (axis == RotationAxis.AxisX) {
                            rotationX = 180f
                        } else {
                            rotationY = 180f
                        }
                    },
            ) {
                back()
            }
        }
    }
}

@Composable
fun FlipCardImp(
    modifier: Modifier,
    currentCardFace: CardFace,
    front: (@Composable () -> Unit)? = null,
    back: (@Composable () -> Unit)? = null


) {
    //var currentCardFace by remember { mutableStateOf(CardFace.Front) }

    FlipCard(
        cardFace = currentCardFace,
        onClick = {
            //currentCardFace = it.next
        },
        modifier = modifier,
        front = front ?: {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Blue),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Frente", color = Color.White)
            }
        },
        back = back ?: {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Red),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Parte posterior", color = Color.White)
            }
        })
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun FlippableCardPreview() {
    TheHauntedHouseTheme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

            var currentCardFace by remember { mutableStateOf(CardFace.Front) }

            FlipCard(
                cardFace = currentCardFace,
                onClick = {
                    currentCardFace = it.next
                },
                modifier = Modifier
                    .size(200.dp),
                front = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Blue),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Frente", color = Color.White)
                    }
                },
                back = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Red),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Parte posterior", color = Color.White)
                    }
                })
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