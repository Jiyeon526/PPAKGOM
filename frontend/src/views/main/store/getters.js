// 플랫폼 관련 정보 가져오기
export function getIsDesktopPlatform(state) {
  return state.isDesktopPlatform
}
// 메뉴 객체 가져오기
export function getMenus(state) {
  return state.menus
}
// Active된 메뉴 인덱스 가져오기
export function getActiveMenuIndex(state) {
  const keys = Object.keys(state.menus)
  return keys.findIndex(item => item === state.activeMenu)
}
//로그인 확인
export function isLoggedIn(state) {
  if (state.accessToken) return true;
  return false;
}

export function userId(state) {
  return state.userId
}

// 검색한 문자열 반환
export function getTitle(state) {
  console.log(state.searchData);
  return state.searchData
}

export function getNaverAccessToken(state) {
  return state.naveraccessToken;
}

export function isNaverLoggedIn(state) {
  if (state.naveraccessToken) return true;
  return false;
}

export function getKakaoIsLoggedIn(state) {
  return state.kakaologin;
}

export function getGoogleIsLoggedIn(state) {
  return state.googlelogin;
}

export function getUserpk(state) {
  return state.userpk;
}

export function getStudypk(state) {
  return state.studypk;
}

export function getSelectOption(state) {
  return state.selectOption;
}

export function getReload(state) {
  return state.reload;
}

export function getJoinStudyList(state) {
  return state.joinStudyList;
}

