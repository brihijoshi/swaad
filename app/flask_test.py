from flask import Flask, request, Response
import os
import json
from werkzeug import utils
from aws_detect import detect_labels_local_file
from food2fork import get_recipes


app = Flask(__name__)
label_list = []


@app.route('/ingredient', methods=['POST'])
def ingredient():
    print("hit ingredient endpoint")
    image_file = request.files['image']
    max = get_latest_file_id()
    filename = utils.secure_filename(str(max + 1))
    image_file.save("images/{}.jpg".format(filename))
    label = detect_labels_local_file("images/{}.jpg".format(filename))
    print(label)
    if label is not None:
        label_list.append(label)

    return Response(response=None, status=200, mimetype="application/json")


@app.route('/recipe', methods=['POST'])
def recipe():
    if len(label_list) == 0:
        return Response(response="No ingredients scanned", status=404, mimetype="application/json")

    print(str(label_list))
    response = get_recipes(label_list)
    label_list.clear()
    clear_files()

    return Response(response=json.dumps({"results": response}), status=200, mimetype="application/json")


def clear_files():
    for file in os.listdir("images"):
        if file.endswith("jpg"):
            os.unlink(os.path.join("images", file))


def get_latest_file_id():
    max = 1
    for file in os.listdir("images"):
        if file.endswith("jpg"):
            file_id = file.replace(".jpg", "")
            if int(file_id) > max:
                max = int(file_id)
    return max


# start flask app
clear_files()
app.run(host="0.0.0.0", port=80)