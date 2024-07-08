from flask import Flask, request, jsonify
from flask_cors import CORS
from werkzeug.datastructures import FileStorage

app = Flask(__name__)
CORS(app)


@app.route('/upload', methods=['POST'])
def upload_audio():
    audio_file: FileStorage = request.files['audio']
    if audio_file:
        print(F"{audio_file=}")
        # 保存文件或进行其他处理  
        filename = '../received_audio.wav'
        audio_file.save(filename)
        #
        return jsonify({'message': '音频文件已接收并保存为 {}'.format(filename)})
    else:
        return jsonify({'error': '没有上传音频文件'}), 400


if __name__ == '__main__':
    app.run(debug=True)
