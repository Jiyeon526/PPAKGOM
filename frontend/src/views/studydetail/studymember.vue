<template>
  <div>
    <h2 style="text-align:left">MY 스터디 멤버</h2>
    <br>
    <ul v-if="state.memberList.length !== 0" class="membercard-ul">
      <li v-for="memberData in state.memberList" :key="memberData" class="membercard-li" >
        <membercard :memberData='memberData' />
      </li>
    </ul>
    <div v-if="state.curId == state.ownerId">
    <el-divider></el-divider>
    <el-row :gutter="24" >
      <el-col :span="12" align="left">
        <h1 style="display: inline-block; margin-right: 10px">초대한 회원 현황</h1>
        <el-button size="mini" @click="findMember">초대하기</el-button>
      <el-table
        :data="state.sendList"
        height="400"
        @row-click="handleClick"
        style="width: 100%">
        <template #empty>
          <h3>같이 공부할 회원을 초대해보세요!</h3>
        </template>
        <el-table-column
          prop="name"
          label="회원명"
        >
        </el-table-column>
        <el-table-column
          prop="temperature"
          label="열정도"
        >
        </el-table-column>
        <el-table-column
          prop="state"
          align='right'
        >
          <template #default="scope">
            <div v-if="scope.row['state'] == 2">
              <el-button
                size="mini" type="warning"
                @click="handleCancelInvite(scope.$index, scope.row)">초대취소</el-button>
            </div>
            <div v-else>
              <el-button
                size="mini" type="danger"
                @click="handleCheckReject(scope.$index, scope.row)">초대거절</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
      </el-col>

    <el-col :span="12" align="left">
        <h1>가입 요청 회원</h1>
      <el-table
        :data="state.applyList"
        height="400"
        @row-click="handleClick"
        style="width: 100%">
        <template #empty>
          <h3>가입 요청한 회원이 없습니다.</h3>
        </template>
        <el-table-column
          prop="name"
          label="회원명"
        >
        </el-table-column>
        <el-table-column
          prop="temperature"
          label="열정도"
        >
        </el-table-column>
        <el-table-column
          prop="state"
          align='right'
        >
          <template #default="scope">
            <div v-if="scope.row['state'] == 2">
              <el-button
                size="mini" type="primary"
                @click="joinAccept(scope.$index, scope.row)">승인</el-button>
              <el-button
                size="mini" type="danger"
                @click="joinReject(scope.$index, scope.row)">거절</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
      </el-col>
    </el-row>
    </div>
  </div>
</template>

<script>
import { onMounted, reactive, computed } from 'vue'
import { useStore } from 'vuex'
import membercard from './membercard.vue'

export default {
  components: { membercard },

  name: 'studymember',
  setup (props, {emit} ) {
    const store = useStore()
    const state = reactive({
      memberList : [],
      sendList : computed(() => store.getters["root/getSendInviteMemberList"]),
      applyList : [],
      inStudyList : [],
      studypk: computed(() => store.getters["root/getStudypk"]),
      ownerId: null,
      curId: computed(() => store.getters["root/getUserpk"])
   })

    onMounted (() => {
      studyMemberList()
      inviteSendList()
      applyUserList()
      ownerOnly()
    })

    const studyMemberList = function() {
      store.dispatch('root/requestMyStudyMember')
      .then(function(res) {
        for (let i=0; i < res.data.length; i++) {
          const origin_url = res.data[i]["thumbnail"]
          const need_from = origin_url.indexOf('image')
          const url_length = origin_url.length
          const process_thumbnail = origin_url.substring(need_from,url_length)
          res.data[i]["thumbnail"] = process_thumbnail
        }
        state.memberList = res.data
      })
    }

    const inviteSendList = function() {
      store.dispatch("root/requestinviteSendList")
      .then(function(res){
        store.commit('root/setSendInviteMemberList',res.data.inviteResult)
      })
    }

    const applyUserList = function() {
      store.dispatch("root/requestShowApplyList")
      .then(function(res){
        state.applyList = res.data
      })
    }

    const handleClick = function(row, column, cell, event) {
      if (column.property != "state") {
        store.dispatch("root/requestNameUserJoinStudyList",row["name"])
        .then(function(res) {
          state.inStudyList = []
          state.inStudyList = res.data
          store.dispatch("root/requestOtherProfile", row["name"])
          .then(function(res) {
            const profileData = res.data
            const origin_url = profileData["profile_thumbnail"]
            const need_from = origin_url.indexOf('image')
            const url_length = origin_url.length
            const process_thumbnail = origin_url.substring(need_from,url_length)
            profileData["profile_thumbnail"] = process_thumbnail
            emit("openOtherpeopleDialog", profileData, state.inStudyList)
          })
          .catch(function(err) {
            console.log(err)
          })
        })
      }
    }

    const handleCancelInvite = function(index, row) {
      store.dispatch("root/requestCancelInvite", row["userId"])
      .then(function(res) {
        state.sendList.splice(index, 1)
      })
      .catch(function(err) {
        console.log("error",err)
      })
    }

    const handleCheckReject = function(index, row) {
      store.dispatch("root/requestCheckInviteReject", row["userId"])
      .then(function(res) {
        state.sendList.splice(index, 1)
      })
      .catch(function(err) {
        console.log("error",err)
      })
    }

    const findMember = function() {
      emit("openInviteDialog")
    }

    const joinAccept = function(index, row) {
      const body = new FormData()
      body.append("study_id",state.studypk)
      body.append("user_id", row["user_id"])
      store.dispatch("root/requestapplyOkay", body)
      .then(function(res) {
        state.applyList.splice(index, 1)
      })
      .catch(function(err) {
        console.log("error",err)
      })
    }
    const joinReject = function(index, row) {
      const body = new FormData()
      body.append("study_id",state.studypk)
      body.append("user_id", row["user_id"])
      store.dispatch("root/requestapplyReject", body)
      .then(function(res) {
        state.applyList.splice(index, 1)
      })
      .catch(function(err) {
        console.log("error",err)
      })
    }

    const ownerOnly = function() {
      store.dispatch("root/requestStudyInfoDetail", state.studypk)
      .then(function(res) {
        state.ownerId = res.data.studyResult[0]["owner_id"]
      })
    }

  return { state, studyMemberList, inviteSendList, handleClick, handleCancelInvite, handleCheckReject, applyUserList, findMember, joinAccept, joinReject,ownerOnly }
  }
}
</script>

<style>
.membercard-ul {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, max-content));
  grid-gap: 16px;
  justify-content: center;
  padding: initial;
}
.membercard-li {
  list-style-type: none;
  padding: 5px;
  width: 300px;
}
</style>
