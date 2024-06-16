<template>
  <div :class="['message', { 'is-bot': message.isBot, 'is-user': !message.isBot }]">
    <img v-if="message.isBot" src="system-avatar.jpg" alt="System Avatar" class="avatar" />
    <img v-else src="user-avatar.jpg" alt="User Avatar" class="avatar" />
    <div class="text">{{ message.text }}</div>
  </div>
</template>

<script>
export default {
  name: 'ChatMessage',
  props: {
    message: Object,
  },
};
</script>

<style scoped>
.message {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  max-width: 80%;
}
.is-bot {
  flex-direction: row;
}
.is-user {
  flex-direction: row-reverse;
  margin-left: auto;
}
.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin: 0 10px;
}
.text {
  padding: 10px;
  border-radius: 10px;
  position: relative;
}
.is-bot .text {
  background-color: #d5f5e3;
}
.is-user .text {
  background-color: #007bff;
  color: white;
}
.is-bot .text::after, .is-user .text::after {
  content: '';
  position: absolute;
  width: 0;
  height: 0;
  border-style: solid;
}
.is-bot .text::after {
  left: -10px;
  top: 10px;
  border-width: 10px 10px 10px 0;
  border-color: transparent #d5f5e3 transparent transparent;
}
.is-user .text::after {
  right: -10px;
  top: 10px;
  border-width: 10px 0 10px 10px;
  border-color: transparent transparent transparent #007bff;
}
</style>
