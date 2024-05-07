    import Vue from 'vue'
    import Router from 'vue-router'
    import HomePage from '@/components/HomePage'
    import login from '@/views/login'
    import MemberList from '@/views/Member/MemberList'
    import MemberRank from '@/views/Member/MemberRank'
    import NotFound from '@/views/Errors/NotFound'

    Vue.use(Router)

    export default new Router({
        routes: [
            {
                path: '/Homepage/:name', // 表示路由跳转路径
                name: 'Homepage',
                component: HomePage,
                props:true,
                children: [
                {
                    //用户名单
                    path: '/member/list', // 表示子路由跳转路径
                    name: 'MemberList',
                    component: MemberList
                    // 
                },
                {
                    path: '/member/rank/:id', // 表示子路由跳转路径
                    name: 'MemberRank',
                    component: MemberRank,
                    props:true
                }
                ]
            },
            {
                path: '/login', // 表示路由跳转路径
                name: 'login',
                component: login
            },
            {
                path: '/goLogin', // 重定向 :name 表示
                redirect: '/login',
            },
            {
                path: '/goHome/:name', // 表示路由跳转路径
                redirect: '/Homepage/:name',
            },
            {
                path: '*', // 跳转404页面
                component: NotFound
            },
        ]
    })



