export function setPlatform(state, isDesktop) {
  state.isDesktopPlatform = isDesktop
}

export function setMenuActive(state, index) {
  console.log('setMenuActive', state, index)
  const keys = Object.keys(state.menus)
  state.activeMenu = keys[index]
}

export function setMenuActiveMenuName(state, menuName) {
  state.activeMenu = menuName
}

/////

export function setAccessToken(state) {
  state.accessToken = localStorage.getItem('accessToken')
  console.log(state.accessToken)
}

export function setUserId(state) {
  state.userId = localStorage.getItem('userId')
  console.log(state.userId)
}

export function deleteToken(state) {
  state.accessToken = ''
  state.userId = ''
}

// 검색한 문자열 업데이트
export function titleSearch(state, payload) {
  state.searchData = payload.titleData
  console.log("mutate!!!", state.searchData)
}

export function setNaverAccessToken(state) {
  state.naveraccessToken = localStorage.getItem('naveraccessToken')
  console.log(state.naveraccessToken)
}

export function deleteNaverToken(state) {
  state.naveraccessToken = ''
}

export function setGoogleLogin(state, payload) {
  state.googlelogin = payload.login
}

export function kakaologin(state, payload) {
  state.kakaologin = payload.login
}
