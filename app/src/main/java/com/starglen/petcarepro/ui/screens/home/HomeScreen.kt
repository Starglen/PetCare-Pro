package com.starglen.petcarepro.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.navigation.ROUT_ACTIVITY
import com.navigation.ROUT_HEALTH
import com.navigation.ROUT_HOME
import com.navigation.ROUT_NUTRITION
import com.navigation.ROUT_REMINDER
import com.starglen.petcarepro.ui.theme.maincolor
import com.starglen.petcarepro.R
import com.starglen.petcarepro.ui.theme.newwhite


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var selectedIndex by remember { mutableStateOf(0) }

    Scaffold(

        //BottomBar
        bottomBar = {
            NavigationBar(
                containerColor = maincolor,
                modifier = Modifier.height(70.dp)
            ){
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = selectedIndex == 0,
                    onClick = { selectedIndex = 0
                        //navController.navigate(ROUT_HOME)
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorites") },
                    label = { Text("Favorites") },
                    selected = selectedIndex == 1,
                    onClick = { selectedIndex = 1
                        //navController.navigate(ROUT_HOME)
                    }
                )

                NavigationBarItem(
                    icon = { Icon(Icons.Default.Add, contentDescription = "Profile") },
                    label = { Text("Info") },
                    selected = selectedIndex == 2,
                    onClick = { selectedIndex = 2
                        //  navController.navigate(ROUT_HOME)
                    }
                )

                NavigationBarItem(
                    icon = { Icon(Icons.Default.Info, contentDescription = "Profile") },
                    label = { Text("Info") },
                    selected = selectedIndex == 3,
                    onClick = { selectedIndex = 3
                        //  navController.navigate(ROUT_HOME)
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    selected = selectedIndex == 4,
                    onClick = { selectedIndex = 4
                        //  navController.navigate(ROUT_HOME)
                    })

            }
        },


        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(2.dp)

            ) {

                Row (verticalAlignment = Alignment.CenterVertically){
                    Image(
                        painter = painterResource(R.drawable.paw),
                        contentDescription = "Pets",
                        modifier = Modifier
                            .size(80.dp)
                    )
                    Text(
                        text = "PetCare Pro",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Column (modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "Your pet’s health and care—made simple.",
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Replace with your image asset
                    Image(
                        painter = painterResource(R.drawable.catdog),
                        contentDescription = "Pets",
                        modifier = Modifier
                            .height(250.dp)
                            .fillMaxWidth(),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            navController.navigate(ROUT_REMINDER)
                        },
                        colors = ButtonDefaults.buttonColors(maincolor),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("PetCare Reminder", color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(modifier = Modifier.clickable{navController.navigate(ROUT_HEALTH)}.fillMaxWidth()) {
                        FeatureItem(
                            image = painterResource(id = R.drawable.health),
                            title = "Health Tracker",
                            desc = "Stay on top of your pet’s wellness with ease.\n" +
                                    "The Health Tracker helps you monitor vet visits, vaccinations, and medications—all in one place."
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    Row(modifier = Modifier.clickable{navController.navigate(ROUT_NUTRITION)}.fillMaxWidth()) { FeatureItem(
                        image = painterResource(id = R.drawable.nutrition),
                        title = "PetCare Nutrition",
                        desc = "Discover what’s best for your pet’s diet.\n" +
                                "Get feeding tips, portion guides, and healthy treat ideas.\n" +
                                "Keep every meal balanced and tail-wagging good!"
                    ) }
                    Spacer(modifier = Modifier.height(20.dp))

                    Row (modifier = Modifier.fillMaxWidth()) {
                        FeatureItem(
                            image = painterResource(id = R.drawable.vetlocator),
                            title = "Emergency Vet Locator",
                            desc = "Find nearby emergency vets instantly, anytime.\n" +
                                    "Get directions, contact info, and hours in seconds.\n" +
                                    "Peace of mind when your pet needs it most."
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row (modifier = Modifier.fillMaxWidth().clickable{navController.navigate(
                        ROUT_ACTIVITY
                    )}){
                        FeatureItem(
                            image = painterResource(id = R.drawable.activitylog),
                            title = "Daily Care & Activities",
                            desc = "Keep your pet happy, healthy, and active every day!\n" +
                                    "Track daily routines like walks, playtime, grooming, and more. Build healthy habits and stay on top of your pet’s everyday care—all in one easy-to-use spot."
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))


                }
            }
        }
    )

    //End of scaffold

}



@Composable
fun FeatureItem(image: Painter, title: String, desc: String) {
    Row(modifier = Modifier.padding(vertical = 12.dp)) {
        Image(
            painter = image,
            contentDescription = title,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(title, fontWeight = FontWeight.Bold)
            Text(desc, fontSize = 14.sp, color = Color.Gray)
        }
    }
}




@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    HomeScreen(rememberNavController())
}