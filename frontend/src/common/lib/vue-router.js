import { createRouter, createWebHistory } from 'vue-router'
import Home from '@/views/home/home'
import ConferencesDetail from '@/views/conferences/conference-detail'
import History from '@/views/history/history'
import Mypage from '@/views/mypage/mypage'
import Mainpage from '@/views/mainpage'
import Createpage from '@/views/createpage'
import Studyjoin from '@/views/studyjoin'
import Studylike from '@/views/studylike'
import Studymanage from '@/views/studymanage'

const fullMenu = require('@/views/main/menu.json')
function makeRoutesFromMenu() {
  let routes = Object.keys(fullMenu).map((key) => {
    if (key === 'home') {
      return { path: fullMenu[key].path, name: key, component: Home }
    } else if (key === 'history') {
      return { path: fullMenu[key].path, name: key, component: History }
    } else if (key === 'main') {
      return { path: fullMenu[key].path, name: key, component: Mainpage }
    } else if (key === 'create') {
      return { path: fullMenu[key].path, name: key, component: Createpage }
    }else if (key === 'join') {
      return { path: fullMenu[key].path, name: key, component: Studyjoin }
    }else if (key === 'like') {
      return { path: fullMenu[key].path, name: key, component: Studylike }
    }
    else if (key === 'manage') {
      return { path: fullMenu[key].path, name: key, component: Studymanage }
    }else { // menu.json 에 들어있는 로그아웃 메뉴
      return null
    }
  })
  // 로그아웃 파싱한 부분 제거
  routes = routes.filter(item => item)
  // menu 자체에는 나오지 않는 페이지 라우터에 추가(방 상세보기)
  routes.push({
    path: '/conferences/:conferenceId',
    name: 'conference-detail',
    component: ConferencesDetail
  },
    {
      path: '/:userId',
      name: 'mypage',
      component: Mypage
    },

  )
  return routes
}

const routes = makeRoutesFromMenu()

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.afterEach((to) => {
  console.log(to)
})

export default router
