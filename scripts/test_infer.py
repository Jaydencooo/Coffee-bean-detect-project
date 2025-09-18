import json
import sys
from infer import preprocess_image, softmax
import tensorflow as tf
import numpy as np

def main():
    if len(sys.argv) < 2:
        print("请传入图片路径")
        return

    image_path = sys.argv[1]

    # 加载模型
    interpreter = tf.lite.Interpreter(model_path="scripts/model.tflite")
    interpreter.allocate_tensors()

    input_details = interpreter.get_input_details()
    output_details = interpreter.get_output_details()

    # 预处理图片
    input_tensor = preprocess_image(image_path, input_details[0]['shape'])
    interpreter.set_tensor(input_details[0]['index'], input_tensor)
    interpreter.invoke()

    # 获取输出
    output_data = interpreter.get_tensor(output_details[0]['index'])
    probs = softmax(output_data[0])

    # 读取 labels
    with open("scripts/labels.txt", "r") as f:
        labels = [line.strip() for line in f.readlines()]

    # 构造 defects 列表
    defects = []
    for i, p in enumerate(probs):
        defects.append({
            "type": labels[i] if i < len(labels) else "Unknown",
            "confidence": float(p),
            "bbox": []
        })

    # 输出 JSON
    result = {
        "status": "success",
        "defects": defects
    }

    print(json.dumps(result, ensure_ascii=False, indent=2))

if __name__ == "__main__":
    main()
