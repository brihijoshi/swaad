from flask import Flask, render_template, request, url_for, jsonify, Response
import numpy as np
import cv2
import io
from PIL import Image

app = Flask(__name__)

# route http posts to this method
@app.route('/api/test', methods=['POST'])
def test():
    r = request

    # print(len(r.data))
    # convert string of image data to uint8
    # nparr = np.fromstring(r.data, np.uint8)
    # # decode image
    # img = cv2.imdecode(nparr, cv2.IMREAD_COLOR)

    imageStream = io.BytesIO(r.data)
    imageFile = Image.open(imageStream)
    imageFile.show()


    # do some fancy processing here....

    # build a response dict to send back to client
    response = {'message': 'image received'}
    # encode response using jsonpickle
    # response_pickled = jsonpickle.encode(response)

    return Response(response=response, status=200, mimetype="application/json")


# start flask app
app.run(host="0.0.0.0", port=5000)