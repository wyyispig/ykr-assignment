<template>
  <el-form ref="form" :model="form" :rules="rules" class="login-box">
    <h2 class="login-title">欢迎登录科大讯飞英语学习平台</h2>
    <el-form-item label="账号" prop="name">
      <el-input type="text" placeholder="请输入用户名" v-model="form.name">
      </el-input>
    </el-form-item>
    <el-form-item label="密码" prop="password">
      <el-input
        type="password"
        placeholder="请输入密码"
        v-model="form.password"
      >
      </el-input>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submitForm('form')">登录</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
export default {
  name: "LogIn",
  data() {
    return {
      form: {
        name: "",
        password: "",
      },
      rules: {
        name: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          {
            min: 6,
            max: 18,
            message: "长度在 6 到 18 个字符",
            trigger: "blur",
          },
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          {
            min: 6,
            max: 18,
            message: "长度在 6 到 18 个字符",
            trigger: "blur",
          },
        ],
      },
    };
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$router.push("/HomePage");
        } else {
          this.$message({
            message: "请输入正确的用户名与密码",
            type: "warning",
          });
        }
      });
    },
  },
};
</script>

<style>
.login-box {
  width: 400px;
  margin: 150px auto;
  border: 2px solid #409eff;
  padding: 40px;
  border-radius: 20px;

}
.login-title{
  text-align: center;
}
</style>