import Vue from 'vue'
import Router from 'vue-router'
import ModelList from '@/components/ModelList'
import ModelCreation from '@/components/ModelCreation'
import Login from '@/components/Login'
import Types from '@/components/Types'
import TypeCreation from '@/components/TypeCreation'

import Alf from '@/alfresco/alfresco.js'

Vue.use(Router)

var router = new Router({
  routes: [
    {
      path: '/',
      component: ModelList
    },
    {
      path: '/create',
      component: ModelCreation
    },
    {
      path: '/types/:modelName',
      component: Types,
      props: true
    },
    {
      path: '/type/create/:modelName',
      component: TypeCreation,
      props: true
    },
    {
      path: '/login',
      component: Login
    }
  ]
})

router.beforeEach((to, from, next) => {
  if (!to.matched.some(record => record.path === '/login')) {
    if (Alf.isLoggedIn()) {
      next()
    } else {
      next({path: '/login'})
    }
  } else {
    next()
  }
})

export default router
