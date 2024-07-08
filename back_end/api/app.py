import dataclasses

from flask import Flask, request
from flask_cors import CORS
from flask_migrate import Migrate
from flask_sqlalchemy import SQLAlchemy

app = Flask(__name__)
CORS(app)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///test.db'

# db
db = SQLAlchemy(app)


class User(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(80), unique=True, nullable=False)
    password = db.Column(db.String(80))


migrate = Migrate(app, db)


@app.route('/auth/signIn', methods=['POST'])
def sign_in():
    print(F"{request.json=}")
    j = request.json
    c = User.query.filter_by(username=j['username'], password=j['password']).count()
    return {
        "success": c > 0,
        "message": '' if c > 0 else '账号密码错误',
    }


@app.route('/auth/signUp', methods=['POST'])
def sign_up():
    print(F"{request.json=}")
    j = request.json
    c = User.query.filter_by(username=j['username']).count()
    is_exists = c > 0
    if is_exists:
        return {
            "success": False,
            "message": '已经存在该账号',
        }
    db.session.add(User(username=j['username'], password=j['password']))
    db.session.commit()
    return {
        "success": True,
    }


@app.route('/test/message')
def _message():
    return 'test message'


if __name__ == '__main__':
    app.run(debug=True)
