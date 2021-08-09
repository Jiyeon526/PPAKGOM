// API
import axios from 'axios'
import $axios from 'axios'

// 로그인
export function requestLogin({ state }, payload) {
  console.log('requestLogin', state, payload)
  const url = '/users/login'
  let body = payload
  return $axios.post(url, body)
}

// 소셜로그인
export function requestSocialLogin({ state }, payload) {
  console.log('requestLogin', state, payload)
  const url = 'users/sociallogin'
  let body = payload
  return $axios.post(url, body)
}

//회원가입
export function requestRegister({ state }, payload) {
  console.log('requestRegister', state, payload)
  const headers = {'Content-Type': 'multipart/form-data' } // 토큰
  const url = '/users/register'
  let body = payload
  return $axios.post(url, body,{ headers: headers })
}

// 로그아웃
export function requestLogout({ state }) {
  console.log('requestLogout', state)
  localStorage.removeItem('accessToken')
  localStorage.removeItem('userId')
  return
}

//아이디 중복체크
export function requestCheckDuplicate({ state }, payload) {
  console.log('requestCheckDuplicate', state, payload)
  const url = `/users/${payload.id}/userid`
  return $axios.get(url)
}

//닉네임 중복체크
export function requestNameCheckDuplicate({ state }, payload) {
  console.log('requestNameCheckDuplicate', state, payload)
  const url = `/users/${payload.id}/name`
  return $axios.get(url)
}

// 이메일 인증
export function requestEmail({ state }, payload) {
  console.log('requestEmail', state, payload)
  const url = '/users/email'
  let body = payload
  return $axios.post(url, body)
}

// 이메일 인증
export function requestEmailCode({ state }, payload) {
  console.log('requestEmailCode', state, payload)
  const url = '/users/verifyCode'
  let body = payload
  return $axios.post(url, body)
}

// 내 프로필
export function requestReadMyInfo({ state }) {
  console.log('requestMyInfo', state)
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = '/users/me'
  return $axios.get(url, { headers: headers })
}

// 유저 정보 삭제
export function requestDeleteMyInfo({ state }) {
  console.log('requestMyInfo', state)
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/users/${state.userId}`
  return $axios.delete(url, { headers: headers })
}

// 유저 정보 수정
export function requestUpdateMyInfo({ state }, payload) {
  console.log('requestMyInfo', state, payload)
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/users/${state.userId}`
  let body = payload
  console.log("updateInfo");
  console.log(headers);
  return $axios.patch(url, body, { headers: headers })
}

// 방 생성하기
export function requestCreateRoom({ state }, payload) {
  console.log('requestCreateRoom', state, payload)
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '', 'Content-Type': 'multipart/form-data' } // 토큰
  const url = '/conferences/conferences'
  let body = payload
  return $axios.post(url, body, { headers: headers })
}

// 스터디 목록 가져오기
export function requestStudyList({state}) {
  const url = '/study/'
  return $axios.get(url)
}

// 추천 스터디 목록 가져오기
export function requestRecommendStudyList({state}) {
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/study/interest/4`
  return $axios.get(url, { headers: headers })
}

// 검색어 스터디 목록 가져오기
export function requestSearchStudyList({state}, param) {
  console.log(param)
  let url = '/study/'
  if (param.option === 1) {
    url += `?studyId=${param.searchValue}`
  }
  if (param.option === 2) {
    url += `?name=${param.searchValue}`
  }
  if (param.option === 3) {
    url += `?interest=${param.searchValue}`
  }
  return $axios.get(url)
}

//방 상세 정보 얻어오기
export function requestRoomInfoDetail({ state }, payload) {
  console.log('requestRoomInfoDetail', state)
  console.log(payload);
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/conferences/${payload}`
  return $axios.get(url, { headers: headers })
}

export function requestNaverLogout({ state }) {
  console.log('requestLogout', state)
  localStorage.removeItem('naveraccessToken')
  localStorage.removeItem('com.naver.nid.access_token')
  localStorage.removeItem('com.naver.nid.oauth.state_token')
  return
}

// 가입한 스터디 목록 가져오기
export function requestJoinStudyList({state}) {
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/users/join/${state.userpk}`
  return $axios.get(url, { headers: headers })
}

// 찜한 스터디 목록 가져오기
export function requestLikeStudyList({state}) {
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/study/like/${state.userpk}`
  return $axios.get(url, { headers: headers })
}
