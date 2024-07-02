import Vue from 'vue';
import Router from 'vue-router';
import HomePage from '@/components/HomePage';
import ChatBot from '@/views/ChatBot';
import MemberList from '@/views/Member/MemberList';
import MemberRank from '@/views/Member/MemberRank';
import NotFound from '@/views/Errors/NotFound';
import Login from '@/views/login'; // 确保导入 login 组件

Vue.use(Router);

export default new Router({
  routes: [
    {
      path: '/Homepage/:name',
      name: 'Homepage',
      component: HomePage,
      props: true,
      children: [
        {
          path: 'editEssay',
          name: 'EditEssay',
          component: ChatBot,
          props: true // 确保使用 props 传递参数
        },
        {
          path: 'member/list',
          name: 'MemberList',
          component: MemberList,
          props: true
        },
        {
          path: 'member/rank/:id',
          name: 'MemberRank',
          component: MemberRank,
          props: true,
        }
      ],
    },
    {
      path: '/login',
      name: 'login',
      component: Login, // 使用正确的组件名称
    },
    {
      path: '/goLogin',
      redirect: '/login',
    },
    {
      path: '/goHome/:name',
      redirect: '/Homepage/:name',
    },
    {
      path: '*',
      component: NotFound,
    },
  ],
});
