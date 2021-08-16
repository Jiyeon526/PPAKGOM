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
    const url = `/users/social`
    return $axios.post(url, payload)
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
  localStorage.removeItem('userPk')
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

// 이메일 코드 인증
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
  const url = '/study/'
  let body = payload
  return $axios.post(url, body, { headers: headers })
}

// 스터디 목록 가져오기
export function requestStudyList({state}) {
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = '/study/'
  return $axios.get(url, { headers: headers })
}

// 추천 스터디 목록 가져오기
export function requestRecommendStudyList({state}) {
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/study/interest/${state.userpk}`
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
 // localStorage.removeItem('com.naver.nid.access_token')
 // localStorage.removeItem('com.naver.nid.oauth.state_token')
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
  const url = `/users/like/${state.userpk}`
  return $axios.get(url, { headers: headers })
}

// 가입 신청 현황 목록 가져오기
export function requestAskJoinList({state}) {
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/join/response/${state.userpk}`
  return $axios.get(url, { headers: headers })
}

// 가입 신청 취소 가져오기
export function requestCancelJoin({state}, payload ) {
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/join/response/cancel/${state.userpk}`
  const params = payload
  return $axios.delete(url, { headers: headers, params:params })
}

// 스터디 방 상세정보 가져오기
export function requestStudyInfoDetail({state},payload) {
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/study/${payload}/detail`
  return $axios.get(url, { headers: headers })
}

// 초대받은 현황 목록 가져오기
export function requestinviteReceiveList({state}) {
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/invitation/request/${state.userpk}`
  return $axios.get(url, { headers: headers })
}

// 유저 프로필 확인
export function requestOtherProfile({state}, payload) {
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  console.log("여기",payload)
  const url = `/users/${payload}/profile`
  return $axios.get(url, { headers: headers })
}

// 특정 유저가 가입한 스터디 목록 가져오기(**닉네임 이용해서)
// export function requestUserJoinStudyList({state},payload) {
//   const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
//   const url = `/users/join/${payload}`
//   return $axios.get(url, { headers: headers })
// }

// 스터디원 평가 목록 가져오기
export function requestEvaluateMemberList({state}) {
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/study/rating/${state.userpk}`
  return $axios.get(url, { headers: headers })
}

// 평가 완료 후 데이터 보내기
export function requestSendMemberRating({state}, payload) {
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/study/rating/${state.userpk}`
  let body = payload
  return $axios.post(url, body, { headers: headers})
}
// 스터디 가입하기
export function requestJoinStudy({state}, payload) {
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/join/${state.userpk}?studyId=${payload.studyId}`
  return $axios.post(url, { headers: headers })
}

// 스터디 찜하기
export function requestLikeStudy({state}, payload) {
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/users/like/${state.userpk}?studyId=${payload.studyId}`
  return $axios.post(url, { headers: headers })
}

// 스터디 찜하기 취소
export function requestLikeCancleStudy({state}, payload) {
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/users/like/${state.userpk}?studyId=${payload.studyId}`
  return $axios.delete(url, { headers: headers })
}

// 스터디 출석하기
export function requestAttendence({state}, payload) {
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/study/${payload.studyId}/attend/${payload.userpk}`
  return $axios.post(url, {}, { headers: headers })
}
