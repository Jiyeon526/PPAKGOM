// API
import axios from 'axios'
import $axios from 'axios'

// 로그인
export function requestLogin({ state }, payload) {

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

  const headers = {'Content-Type': 'multipart/form-data' } // 토큰
  const url = '/users/register'
  let body = payload
  return $axios.post(url, body,{ headers: headers })
}

// 로그아웃
export function requestLogout({ state }) {

  localStorage.removeItem('accessToken')
  localStorage.removeItem('userId')
  localStorage.removeItem('userPk')
  return
}

//아이디 중복체크
export function requestCheckDuplicate({ state }, payload) {

  const url = `/users/${payload.id}/userid`
  return $axios.get(url)
}

//닉네임 중복체크
export function requestNameCheckDuplicate({ state }, payload) {

  const url = `/users/${payload.id}/name`
  return $axios.get(url)
}

// 이메일 인증
export function requestEmail({ state }, payload) {

  const url = '/users/email'
  let body = payload
  return $axios.post(url, body)
}

// 이메일 코드 인증
export function requestEmailCode({ state }, payload) {

  const url = '/users/verifyCode'
  let body = payload
  return $axios.post(url, body)
}

// 내 프로필
export function requestReadMyInfo({ state }) {

  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = '/users/me'
  return $axios.get(url, { headers: headers })
}

// 유저 정보 삭제
export function requestDeleteMyInfo({ state }) {

  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/users/${state.userId}`
  return $axios.delete(url, { headers: headers })
}

// 유저 정보 수정
export function requestUpdateMyInfo({ state }, payload) {

  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/users/${state.userpk}`
  let body = payload

  return $axios.put(url, body, { headers: headers })
}

// 방 생성하기
export function requestCreateRoom({ state }, payload) {

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


  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/conferences/${payload}`
  return $axios.get(url, { headers: headers })
}

export function requestNaverLogout({ state }) {

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

// 가입 신청 거절 확인
export function requestCheckJoinReject({state}, payload ) {
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/join/response/reject/${state.userpk}`
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
  const url = `/users/${payload}/profile`
  return $axios.get(url, { headers: headers })
}

// 특정 유저가 가입한 스터디 목록 가져오기(**닉네임 이용해서)
export function requestNameUserJoinStudyList({state},payload) {
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/users/join/name/${payload}`
  return $axios.get(url, { headers: headers })
}

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

// 스터디 멤버 리스트
export function requestMyStudyMember({state}) {
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/study/${state.studypk}/info/member`
  return $axios.get(url, { headers: headers })
}

// 초대 승인 결과 보내기
export function requestSendAccept({state},payload) {
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/invitation/request/ok/${state.userpk}`
  let body = payload
  return $axios.post(url, body, { headers: headers })
}

// 초대 거절 결과 보내기
export function requestSendReject({state},payload) {
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/invitation/request/reject/${state.userpk}`
  let body = payload

  return $axios.put(url, body, { headers: headers })
}

// 문제집 리스트
export function requestWorkbookList({state}) {
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/study/${state.studypk}/workbook`
  return $axios.get(url, { headers: headers })
}

// 클릭한 문제집 정보 가져오기
export function requestWorkbookInfo({state}, payload) {
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/study/${state.studypk}/workbook/${payload}`
  return $axios.get(url, { headers: headers })
}

// 초대한 현황 목록 가져오기
export function requestinviteSendList({state}) {
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/study/${state.studypk}/invitelist`
  return $axios.get(url, { headers: headers })
}

// 초대 취소하기
export function requestCancelInvite({state}, payload ) {
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/invitation/response/calcel/${state.userpk}`
  const params = {receiverId: payload, studyId: state.studypk }

  return $axios.delete(url, { headers: headers, params:params })
}

// 초대 거절 확인
export function requestCheckInviteReject({state}, payload ) {
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/invitation/response/reject/${state.userpk}`
  const params = {receiverId: payload, studyId: state.studypk }

  return $axios.delete(url, { headers: headers, params:params })
}

// 가입 요청한 회원 목록 가져오기
export function requestShowApplyList({state}) {
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/study/${state.studypk}/joinlist`
  return $axios.get(url, { headers: headers })
}

//////////// 확인 요망!

// 가입 요청 거절
export function requestapplyReject({state}, payload ) {
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/join/request/reject/${state.userpk}`
  const body = payload
  return $axios.put(url, body, { headers: headers })
}

// 가입 요청 승인
export function requestapplyOkay({state}, payload ) {
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/join/request/ok/${state.userpk}`
  const body = payload

  return $axios.post(url, body, { headers: headers })
}

// 스케줄 생성
export function requestCreateSchedule({state}, payload) {
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/study/${state.studypk}/schedule`
  const body = payload
  return $axios.post(url, body, { headers: headers })
}

// 스케줄 정보 가져오기
export function requestScheduleInfo({ state }, payload) {

  const url = `/study/${payload.studyId}/schedule?month=${payload.month}`

  return $axios.get(url)
}

// 스터디 출석하기
export function requestAttendence({state}, payload) {
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/study/${payload.studyId}/attend/${payload.userpk}`
  return $axios.post(url, {}, { headers: headers })
}

// 문제집 만들기
export function requestMakeWorkbook({state}, payload) {
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/study/${state.studypk}`
  const body = payload
  return $axios.post(url, body, { headers: headers })
}

// 문제집 제출 후 채점 결과 확인
export function requestSubmitAnswer({state}, payload) {
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/study/${state.userpk}/score/${state.testpk}`
  const body = payload.answer
  return $axios.post(url, body, { headers: headers })
}

// 해당 관심사 회원 목록
export function requestSearchInterest({state}, payload) {
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/study/${state.studypk}/member/${payload}`
  return $axios.get(url, { headers: headers })
}

// 해당 회원 초대
export function requestInviteMember({ state }, payload) {
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/study/${state.studypk}/member`
  let body = payload
  return $axios.post(url, body, { headers: headers })
}

//문제 푼 점수 결과 가져오기
export function requestScore({ state }, payload) {
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/study/${payload}/score`
  return $axios.get(url,  { headers: headers })
}

//출석 결과 가져오기
export function requestStudyAttend({ state }, payload) {
  const headers = { 'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '' } // 토큰
  const url = `/study/${payload}/attend`
  return $axios.get(url,  { headers: headers })
}

