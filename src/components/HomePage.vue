<template>
  <div>
    <el-container>
      <el-aside width="260px">
        <el-menu :default-openeds="['1']">
          <el-submenu index="1">
            <template slot="title"><i class="el-icon-message"></i>导航一</template>
            <el-menu-item-group>
              <template slot="title">分组一</template>
              <el-menu-item index="1-1">选项1</el-menu-item>
              <el-menu-item index="1-2">选项2</el-menu-item>
            </el-menu-item-group>
            <el-menu-item-group title="分组2">
              <el-menu-item index="1-3">
                <router-link :to="{ name: 'EditEssay', params: { name: userName } }">修改作文</router-link>
              </el-menu-item>
            </el-menu-item-group>
            <el-submenu index="1-4">
              <template slot="title">选项4</template>
              <el-menu-item index="1-4-1">选项4-1</el-menu-item>
            </el-submenu>
          </el-submenu>
          <el-submenu index="2">
            <template slot="title"><i class="el-icon-menu"></i>导航二</template>
            <el-menu-item-group>
              <template slot="title">分组一</template>
              <el-menu-item>
                <router-link :to="{ name: 'MemberList', params: { name: userName } }">回到首页</router-link>
              </el-menu-item>
              <el-menu-item index="2-2">选项2</el-menu-item>
            </el-menu-item-group>
            <el-menu-item-group title="分组2">
              <el-menu-item index="2-3">选项3</el-menu-item>
            </el-menu-item-group>
            <el-submenu index="2-4">
              <template slot="title">选项4</template>
              <el-menu-item index="2-4-1">选项4-1</el-menu-item>
            </el-submenu>
          </el-submenu>
          <el-submenu index="3">
            <template slot="title"><i class="el-icon-setting"></i>用户中心</template>
            <el-menu-item-group>
              <template slot="title">好友中心 </template>
              <el-menu-item>
                <router-link :to="{ name: 'MemberList', params: { name: userName } }">好友列表</router-link>
              </el-menu-item>
              <el-menu-item>
                <router-link :to="{ name: 'MemberRank', params: { id: 3, name: userName } }">好友排名</router-link>
              </el-menu-item>
            </el-menu-item-group>
            <el-menu-item-group title="分组2">
              <el-menu-item index="3-3">选项3</el-menu-item>
            </el-menu-item-group>
            <el-submenu index="3-4">
              <template slot="title">选项4</template>
              <el-menu-item index="3-4-1">选项4-1</el-menu-item>
            </el-submenu>
          </el-submenu>
        </el-menu>
      </el-aside>

      <el-container>
        <el-header style="text-align: right; font-size: 12px">
          <el-dropdown>
            <i class="el-icon-setting" style="margin-right: 15px"></i>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>xxxx1</el-dropdown-item>
              <el-dropdown-item>
                <router-link to="/goLogin">退出登录</router-link>
              </el-dropdown-item>
              <el-dropdown-item>xxxx2</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <span>{{ userName }}</span>
        </el-header>

        <el-main>
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
export default {
  name: "HomePage",
  props: ['name'],
  computed: {
    userName() {
      return this.name || this.$route.params.name;
    }
  }
};
</script>

<style>
.el-header {
  background-color: #e6a23c;
  color: #1818c1;
  line-height: 30px;
}
</style>
