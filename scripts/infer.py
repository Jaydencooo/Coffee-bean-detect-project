import sys
import json
import tensorflow as tf
from PIL import Image
import numpy as np
import warnings
import os

warnings.filterwarnings("ignore", category=UserWarning)
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'


def preprocess_image(image_path, input_shape):
    mean = [0.1726, 0.1515, 0.1427]
    std = [0.0736, 0.0622, 0.0593]

    image = Image.open(image_path).convert('RGB')

    if input_shape[1] == 3:
        height, width = input_shape[2], input_shape[3]
    else:
        height, width = input_shape[1], input_shape[2]

    image = image.resize((width, height))
    image_np = np.array(image).astype(np.float32) / 255.0
    image_np = (image_np - mean) / std

    if input_shape[1] == 3:
        image_np = np.transpose(image_np, (2, 0, 1))

    input_tensor = np.expand_dims(image_np, axis=0).astype(np.float32)
    return input_tensor


def softmax(logits):
    exp_logits = np.exp(logits - np.max(logits))
    return exp_logits / np.sum(exp_logits)


def main():
    if len(sys.argv) < 2:
        print("请传入图片路径")
        return

    image_path = sys.argv[1]

    interpreter = tf.lite.Interpreter(model_path="scripts/model.tflite")
    interpreter.allocate_tensors()
    input_details = interpreter.get_input_details()
    output_details = interpreter.get_output_details()

    input_tensor = preprocess_image(image_path, input_details[0]['shape'])
    interpreter.set_tensor(input_details[0]['index'], input_tensor)
    interpreter.invoke()

    output_data = interpreter.get_tensor(output_details[0]['index'])
    probs = softmax(output_data[0])

    # 读取 labels
    with open("scripts/labels.txt", "r") as f:
        labels = [line.strip() for line in f.readlines()]

    # 输出完整类别概率数组
    result = {
        "image_path": image_path,
        "output": probs.tolist()  # 数组长度和 labels 一致
    }

    print(json.dumps(result, ensure_ascii=False))
    sys.stdout.flush()


if __name__ == "__main__":
    main()
