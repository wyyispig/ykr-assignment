<template>
  <div class="chat-bot">
    <div class="messages">
      <ChatMessage
        v-for="(message, index) in messages"
        :key="index"
        :message="message"
      />
    </div>
    <div class="input-area">
      <input v-model="userMessage" @keyup.enter="sendMessage" placeholder="请输入你的作文..." />
      <button @click="sendMessage">发送</button>
    </div>
  </div>
</template>

<script>
import ChatMessage from '../components/ChatMessage.vue';

export default {
  name: 'ChatBot',
  components: {
    ChatMessage,
  },
  data() {
    return {
      messages: [
        { text: '请发送您的作文，我会进行打分和点评', isBot: true }
      ],
      userMessage: '',
    };
  },
  methods: {
    sendMessage() {
      if (this.userMessage.trim() !== '') {
        this.messages.push({ text: this.userMessage, isBot: false });
        this.userMessage = '';

        // 模拟后端大模型的评价
        setTimeout(() => {
          this.messages.push({ text: '这里应该是后端大模型的评价', isBot: true });
        }, 500); // 模拟后端响应时间
      }
    },
  },
};
</script>

<style scoped>
.chat-bot {
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
  margin: 0 auto;
  border: 1px solid #ccc;
  border-radius: 8px;
  background-color: #ffffff;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
}

.messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  background-color: #f9f9f9;
  border-bottom: 1px solid #ccc;
}

.input-area {
  display: flex;
  padding: 10px;
  background-color: #fff;
}

input {
  flex: 1;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  outline: none;
}

button {
  padding: 10px;
  margin-left: 10px;
  border: none;
  background-color: #42b983;
  color: white;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

button:hover {
  background-color: #36a572;
}
</style>
