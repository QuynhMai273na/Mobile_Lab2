import 'package:flutter/material.dart';
import 'package:animated_splash_screen/animated_splash_screen.dart';  // Import thư viện này
import 'package:dog_cat_classifier/home.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Cat and Dog Classifier',
      home: const MySplash(),
      debugShowCheckedModeBanner: false,
    );
  }
}

class MySplash extends StatelessWidget {
  const MySplash({super.key});

  @override
  Widget build(BuildContext context) {
    return AnimatedSplashScreen(
      duration: 2000,
      splash: Column(
        children: [
          Image.asset('assets/cat_dog_icon.png', width: 100, height: 100),
          const SizedBox(height: 20),
          const Text(
            'Cat and Dog Classifier',
            style: TextStyle(
              fontWeight: FontWeight.bold,
              fontSize: 25,
              color: Colors.white,
            ),
          ),
        ],
      ),
      nextScreen: const Home(),
      splashTransition: SplashTransition.fadeTransition,  // Kiểm tra lỗi tại đây
      backgroundColor: Colors.blueAccent,
    );
  }
}
