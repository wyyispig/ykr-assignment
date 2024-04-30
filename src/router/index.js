import Vue from 'vue'
import Router from 'vue-router'
import HomePage from '@/components/HomePage'
import login from '@/views/login'
import MemberList from '@/views/Member/MemberList'
import MemberRank from '@/views/Member/MemberRank'

Vue.use(Router)

export default new Router({
    routes: [
        {
            path: '/Homepage', // 表示路由跳转路径
            name: 'Homepage',
            component: HomePage,
            children: [
            {
                //用户名单
                path: '/member/list', // 表示子路由跳转路径
                name: 'MemberList',
                component: MemberList
                // 
            },
            {
                path: '/member/rank', // 表示子路由跳转路径
                name: 'MemberRank',
                component: MemberRank
            }
            ]
        },
        {
            path: '/login', // 表示路由跳转路径
            name: 'login',
            component: login
        },
    ]
})



