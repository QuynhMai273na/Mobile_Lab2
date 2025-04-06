# code import mô hình
# from transformers import AutoModel, AutoTokenizer

# # Tải mô hình PhoBERT và tokenizer
# model_name = "wonrax/phobert-base-vietnamese-sentiment"
# tokenizer = AutoTokenizer.from_pretrained(model_name)
# model = AutoModel.from_pretrained(model_name)

# # Lưu mô hình về local
# model.save_pretrained("./phobert_model")
# tokenizer.save_pretrained("./phobert_model")



from flask import Flask, request, jsonify
from transformers import AutoModelForSequenceClassification, AutoTokenizer
import torch

# Khởi tạo Flask app
app = Flask(__name__)

# Đường dẫn đến mô hình đã tải về
model_path = "D:\\phobert_model"

# Load mô hình và tokenizer 
model = AutoModelForSequenceClassification.from_pretrained(model_path)
tokenizer = AutoTokenizer.from_pretrained(model_path)

# Hàm dự đoán cảm xúc
def predict_sentiment(text):
    inputs = tokenizer(text, return_tensors="pt", truncation=True, padding=True)
    with torch.no_grad():
        outputs = model(**inputs)
    logits = outputs.logits
    prediction = torch.argmax(logits, dim=-1).item()
    return prediction

@app.route("/predict", methods=["POST"])
def predict():
    data = request.json
    text = data.get("text")
    if not text:
        return jsonify({"error": "No text provided"}), 400
    
  
    prediction = predict_sentiment(text)
    
    return jsonify({"prediction": prediction})

if __name__ == "__main__":
    # Lắng nghe chỉ trên localhost
    app.run(host="127.0.0.1", port=5000, debug=True)
