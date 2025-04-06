//
// import 'package:flutter/material.dart';
// import 'package:image_picker/image_picker.dart';
// import 'package:tflite_flutter/tflite_flutter.dart' as tfl;
// import 'dart:io';
//
// class Home extends StatefulWidget {
//   const Home({super.key});
//   @override
//   _HomeState createState() => _HomeState();
// }
//
// class _HomeState extends State<Home> {
//   bool _loading = true;
//   File? _image;
//   List<dynamic>? _output;
//   final picker = ImagePicker();
//   late tfl.Interpreter _interpreter;
//
//   @override
//   void initState() {
//     super.initState();
//     loadModel();
//   }
//
//   Future<void> loadModel() async {
//     try {
//       _interpreter = await tfl.Interpreter.fromAsset('assets/model_unquant.tflite');
//       print("Model loaded successfully!");
//     } catch (e) {
//       print("Error loading model: $e");
//     }
//   }
//
//   Future<void> detectImage(File image) async {
//     List<int> imageBytes = await image.readAsBytes();
//     var input = preprocessImage(imageBytes);
//     var output = List.filled(2, 0).reshape([1, 2]);
//     _interpreter.run(input, output);
//     setState(() {
//       _output = output;
//       _loading = false;
//     });
//   }
//
//   List<List<double>> preprocessImage(List<int> imageBytes) {
//     List<List<double>> input = List.generate(1, (i) => List.filled(224 * 224 * 3, 0.0));
//     for (int i = 0; i < imageBytes.length; i++) {
//       input[0][i] = imageBytes[i] / 255.0;
//     }
//     return input;
//   }
//
//   pickImage() async {
//     var image = await picker.pickImage(source: ImageSource.camera);
//     if (image == null) return;
//     setState(() {
//       _image = File(image.path);
//       _loading = true;
//     });
//     detectImage(_image!);
//   }
//
//   pickGalleryImage() async {
//     var image = await picker.pickImage(source: ImageSource.gallery);
//     if (image == null) return;
//     setState(() {
//       _image = File(image.path);
//       _loading = true;
//     });
//     detectImage(_image!);
//   }
//
//   @override
//   Widget build(BuildContext context) {
//     return Scaffold(
//       backgroundColor: Colors.blueAccent,
//       body: Container(
//         padding: EdgeInsets.symmetric(horizontal: 24),
//         child: Column(
//           crossAxisAlignment: CrossAxisAlignment.start,
//           children: <Widget>[
//             SizedBox(height: 50),
//             Text("Cat and Dog Classifier",
//               style: TextStyle(color: Colors.white, fontSize: 30, fontWeight: FontWeight.w500),
//             ),
//             SizedBox(height: 50),
//             Center(
//               child: _loading
//                   ? Container(
//                 width: 350,
//                 child: Column(
//                   children: <Widget>[
//                     Image.asset("assets/cat_dog_icon.png"),
//                     SizedBox(height: 50),
//                   ],
//                 ),
//               ) : Container(
//                 child: Column(
//                   children: <Widget>[
//                     Container(
//                       height: 250,
//                       child: Image.file(_image!),
//                     ),
//                     SizedBox(height: 20),
//                     _output != null
//                         ? Text(
//                       "Prediction: ${_output![0][0] > _output![0][1] ? 'Dog' : 'Cat'}",
//                       style: TextStyle(fontSize: 18, color: Colors.white),
//                     )
//                         : Container(),
//                   ],
//                 ),
//               ),
//             ),
//             SizedBox(height: 20),
//             Center(
//               child: Column(
//                 mainAxisAlignment: MainAxisAlignment.center,
//                 children: <Widget>[
//                   GestureDetector(
//                     onTap: pickImage,
//                     child: Container(
//                       width: MediaQuery.of(context).size.width - 250,
//                       alignment: Alignment.center,
//                       padding: EdgeInsets.symmetric(horizontal: 10, vertical: 18),
//                       decoration: BoxDecoration(
//                         color: Colors.redAccent,
//                         borderRadius: BorderRadius.circular(6),
//                       ),
//                       child: Text("Capture a Photo",
//                         style: TextStyle(color: Colors.white, fontSize: 16),
//                       ),
//                     ),
//                   ),
//                   SizedBox(height: 10),
//                   GestureDetector(
//                     onTap: pickGalleryImage,
//                     child: Container(
//                       width: MediaQuery.of(context).size.width - 250,
//                       alignment: Alignment.center,
//                       padding: EdgeInsets.symmetric(horizontal: 10, vertical: 18),
//                       decoration: BoxDecoration(
//                         color: Colors.redAccent,
//                         borderRadius: BorderRadius.circular(6),
//                       ),
//                       child: Text("Select a Photo",
//                         style: TextStyle(color: Colors.white, fontSize: 16),
//                       ),
//                     ),
//                   ),
//                 ],
//               ),
//             ),
//           ],
//         ),
//       ),
//     );
//   }
// }

import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:tflite_flutter/tflite_flutter.dart' as tfl;
import 'dart:io';
import 'dart:typed_data';
import 'package:image/image.dart' as img;

class Home extends StatefulWidget {
  const Home({super.key});

  @override
  _HomeState createState() => _HomeState();
}

class _HomeState extends State<Home> {
  bool _loading = true;
  File? _image;
  List<dynamic>? _output;
  final picker = ImagePicker();
  tfl.Interpreter? _interpreter; // Kiểm tra null trước khi sử dụng

  @override
  void initState() {
    super.initState();
    loadModel();
  }

  /// Load model TFLite
  Future<void> loadModel() async {
    try {
      _interpreter = await tfl.Interpreter.fromAsset('assets/model_unquant.tflite');
      print("✅ Model loaded successfully!");
    } catch (e) {
      print("❌ Error loading model: $e");
    }
  }

  /// Xử lý ảnh đầu vào: resize về 224x224, chuẩn hóa giá trị pixel
  List<List<List<List<double>>>> preprocessImage(File imageFile) {
    Uint8List imageBytes = imageFile.readAsBytesSync();
    img.Image? image = img.decodeImage(imageBytes);

    if (image == null) {
      throw Exception("Cannot decode image!");
    }

    // Resize ảnh về 224x224
    img.Image resizedImage = img.copyResize(image, width: 224, height: 224);

    // Chuyển đổi thành tensor [1, 224, 224, 3]
    List<List<List<List<double>>>> input = List.generate(
      1,
          (_) => List.generate(224, (_) => List.generate(224, (_) => List.filled(3, 0.0))),
    );

    for (int y = 0; y < 224; y++) {
      for (int x = 0; x < 224; x++) {
        img.Pixel pixel = resizedImage.getPixel(x, y);
        input[0][y][x][0] = pixel.r / 255.0;
        input[0][y][x][1] = pixel.g / 255.0;
        input[0][y][x][2] = pixel.b / 255.0;
      }
    }
    return input;
  }

  /// Phát hiện ảnh
  Future<void> detectImage(File image) async {
    if (_interpreter == null) {
      print("❌ Error: Model not loaded");
      return;
    }

    try {
      var input = preprocessImage(image);
      var output = List.generate(1, (i) => List.filled(2, 0.0));

      _interpreter!.run(input, output);

      setState(() {
        _output = output;
        _loading = false;
      });

      print("🔍 Prediction: ${_output![0]}");
    } catch (e) {
      print("❌ Error during image detection: $e");
    }
  }

  /// Chụp ảnh từ camera
  Future<void> pickImage() async {
    var image = await picker.pickImage(source: ImageSource.camera);
    if (image == null) return;
    setState(() {
      _image = File(image.path);
      _loading = true;
    });
    detectImage(_image!);
  }

  /// Chọn ảnh từ thư viện
  Future<void> pickGalleryImage() async {
    var image = await picker.pickImage(source: ImageSource.gallery);
    if (image == null) return;
    setState(() {
      _image = File(image.path);
      _loading = true;
    });
    detectImage(_image!);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.blueAccent,
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 24),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: <Widget>[
              const SizedBox(height: 50),
              const Text(
                "Cat and Dog Classifier",
                style: TextStyle(color: Colors.white, fontSize: 30, fontWeight: FontWeight.w500),
              ),
              const SizedBox(height: 20),
              Expanded(
                child: Center(
                  child: _loading
                      ? Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: <Widget>[
                      Image.asset("assets/cat_dog_icon.png", width: 250),
                      const SizedBox(height: 20),
                      const CircularProgressIndicator(color: Colors.white),
                    ],
                  )
                      : Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: <Widget>[
                      Image.file(_image!, height: 250),
                      const SizedBox(height: 20),
                      _output != null
                          ? Text(
                        "Prediction: ${_output![0][0] < _output![0][1] ? 'Dog' : 'Cat'}",
                        style: const TextStyle(fontSize: 18, color: Colors.white),
                      )
                          : Container(),
                    ],
                  ),
                ),
              ),
              const SizedBox(height: 20),
              Center(
                child: Column(
                  children: <Widget>[
                    GestureDetector(
                      onTap: pickImage,
                      child: Container(
                        width: MediaQuery.of(context).size.width - 250,
                        alignment: Alignment.center,
                        padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 18),
                        decoration: BoxDecoration(
                          color: Colors.redAccent,
                          borderRadius: BorderRadius.circular(6),
                        ),
                        child: const Text(
                          "Capture a Photo",
                          style: TextStyle(color: Colors.white, fontSize: 16),
                        ),
                      ),
                    ),
                    const SizedBox(height: 10),
                    GestureDetector(
                      onTap: pickGalleryImage,
                      child: Container(
                        width: MediaQuery.of(context).size.width - 250,
                        alignment: Alignment.center,
                        padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 18),
                        decoration: BoxDecoration(
                          color: Colors.redAccent,
                          borderRadius: BorderRadius.circular(6),
                        ),
                        child: const Text(
                          "Select a Photo",
                          style: TextStyle(color: Colors.white, fontSize: 16),
                        ),
                      ),
                    ),
                  ],
                ),
              ),
              const SizedBox(height: 20),
            ],
          ),
        ),
      ),
    );
  }
}
