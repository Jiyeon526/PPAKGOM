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
  state.userpk = 0
}

// 검색한 문자열 업데이트
export function titleSearch(state, payload) {
  state.searchData = payload.searchValue
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

export function setkakaologin(state, payload) {
  state.kakaologin = payload.login
}

export function setUserpk(state, payload) {
  state.userpk = payload
}

export function setStudypk(state, payload) {
  state.studypk = payload
}

export function setSelectOption(state, payload) {
  state.selectOption = payload
}

export function setTestpk(state, payload) {
  state.testpk = payload
}

export function setReload(state) {
  state.reload = !state.reload
}

export function setJoinStudyList(state, payload) {
  state.joinStudyList = payload
}

export function setEvaluateMemberList(state, payload) {
  state.evaluateMemberList = payload
}

export function setIsevaluate(state, payload) {
  state.isevaluate = payload
}

export function setDeleteEvaluatedMember(state, payload) {
  for (let i=0; i < state.evaluateMemberList.length; i++) {
    if ((state.evaluateMemberList[i]["study_id"] == payload["studyId"]) &&
    (state.evaluateMemberList[i]["studyMemberId"] == payload["studyMemberId"])) {
      state.evaluateMemberList.splice(i,1)
      break
    }
  }
}

export function setSendInviteMemberList(state, payload) {
  state.inviteSendList = payload
}

export function setWorkbookList(state, payload) {
  state.workbookList = payload
}

export function setproblemCnt(state, payload) {
  state.problemCnt = payload
}
