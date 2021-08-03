// API
import axios from 'axios'
import $axios from 'axios'

// 로그인
export function requestLogin({ state }, payload) {
  console.log('requestLogin', state, payload)
  const url = '/auth/login'
  let body = payload
  return $axios.post(url, body)
}

//회원가입
export function requestRegister({ state }, payload) {
  console.log('requestRegister', state, payload)
  const url = '/users/register'
  let body = payload
  return $axios.post(url, body)
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
  const url = `/users/${payload.id}`
  return $axios.get(url)
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

// 방 목록 가져오기
export function requestConferenceList({ state }, payload) {
  console.log("sea", payload)
  let url = `conferences/conferences?page=${payload.page}&size=${payload.size}`
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  let order  // const로 하면 안됨
  if (payload.conference_category) {
    url += `&conference_category=${payload.conference_category}`
  }
  if (payload.title) {
    url += `&title=${payload.title}`
  }
  if (payload.order) {   // order === ture 이면 오름차순
    order = 'asc'
  }
  else {
    order = 'desc'   // order === false 이면 내림차순
  }
  if (payload.timeSort) {
    url += `&${payload.timeSort}=${order}`
  }
  if (payload.titleeSort) {
    url += `&${payload.titleeSort}=${order}`
  }
  return $axios.get(url, { headers: headers })
}


// 방 목록 Sort
export function requestSortConferenceList({ state }, payload) {
  let url = `conferences/conferences?page=${payload.page}&size=${payload.size}`
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  let order  // const로 하면 안됨
  if (payload.conference_category) {
    url += `&conference_category=${payload.conference_category}`
  }
  if (payload.order) {   // order === ture 이면 오름차순
    order = 'asc'
  }
  else {
    order = 'desc'   // order === false 이면 내림차순
  }
  url += `&${payload.sortType}=${order}`

  return axios.get(url, { headers: headers })
}

// Infinite Scroll 데이터

// 타이틀 검색
// export function requestSearchConference({ state }, payload) {
//   console.log('requestSearchConference', state, payload)
//   const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
//   const url = '/conferences/conferences'
//   const params = payload

//   return $axios.get(url, { params: params, headers: headers })
// }

export function requestCreateRoom({ state }, payload) {
  console.log('requestCreateRoom', state, payload)
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '', 'Content-Type': 'multipart/form-data' } // 토큰
  const url = '/conferences/conferences'
  let body = payload
  return $axios.post(url, body, { headers: headers })
}

//방 상세 정보 얻어오기
export function requestRoomInfoDetail({ state }, payload) {
  console.log('requestRoomInfoDetail', state)
  console.log(payload);
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/conferences/${payload}`
  return $axios.get(url, { headers: headers })
}
