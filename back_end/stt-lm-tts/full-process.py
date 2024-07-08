from config import Config
from m.lite import chat_spark
from stt.Ifasr_new import SpeechToTextApi
from stt.result_utils import get_text

if __name__ == '__main__':
    # STT 部分
    stt_api = SpeechToTextApi(appid=Config.app_id,
                              secret_key=Config.secret_key,
                              upload_file_path=r"../audio/audio_sample_little.wav")

    text = get_text(stt_api.get_result())
    print(F"{text=}")
    # LM 部分
    chat_result = chat_spark(text)
    print(F"{chat_result=}")
