<template>
  <h2>스터디 멤버관리</h2>
  <div v-for="memberData in state.memberList" class="study-membercard">
    <membercard :memberData='memberData' />
  </div>
  <el-divider></el-divider>
  <el-row :gutter="24">
    <el-col :span="12">
      <h1>초대한 회원 현황</h1>
      <el-button>초대하기</el-button>
    <el-table
      :data="state.sendList"
      height="400"
      @row-click="handleClick"
      style="width: 100%">
      <el-table-column
        prop="studyId"
        label="방번호"
      >
      </el-table-column>
      <el-table-column
        prop="studyName"
        label="스터디명"
      >
      </el-table-column>
      <el-table-column
        prop="userName"
        label="회원명"
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

  <el-col :span="12">
      <h1>가입 요청 회원</h1>
    <!-- <el-table
      :data="state.receiveList"
      height="400"
      :show-header="false"
      @row-click="handleUserClick"
      style="width: 100%">
      <el-table-column
        prop="studyId"
        label="study_id"
      >
      </el-table-column>
      <el-table-column
        prop="studyName"
        label="study_name"
      >
      </el-table-column>
      <el-table-column
        prop="userName"
        label="owner_user_name"
      >
      </el-table-column>
      <el-table-column
        prop="state"
        label="buttons"
        align='right'
      >
        <template #default="scope">
          <div v-if="scope.row['state'] == 2">
            <el-button
              size="mini" type="primary"
              @click="inviteAccept(scope.$index, scope.row)">승인</el-button>
            <el-button
              size="mini" type="danger"
              @click="inviteReject(scope.$index, scope.row)">거절</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table> -->
    </el-col>
  </el-row>
</template>

<script>
import { onMounted, reactive } from 'vue'
import { useStore } from 'vuex'
import membercard from './membercard.vue'

export default {
  components: { membercard },

  name: 'studymember',
  setup (props, {emit} ) {
    const store = useStore()
    const state = reactive({
      memberList : [],
      sendList : [],
      inStudyList : [],
   })

    // 페이지 진입시 불리는 훅
    onMounted (() => {
      studyMemberList()
      inviteSendList()
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
        console.log(res)
        state.sendList = res.data.inviteResult
      })
    }

    const handleClick = function(row, column, cell, event) {
      if (column.property != "state") {
        store.dispatch("root/requestNameUserJoinStudyList",row["userName"])
        .then(function(res) {
          state.inStudyList = []
          state.inStudyList = res.data
          store.dispatch("root/requestOtherProfile", row["userName"])
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
      console.log(row)
      store.dispatch("root/requestCancelInvite", {
        study_id: row["studyId"],
        receiver_id: row["userName"]
      })
      .then(function(res) {
        // console.log("!!!!")
        state.joinList.splice(index, 1)
        console.log('123',row)
        console.log(index, state.joinList)
      })
      .catch(function(err) {
        console.log("error",err)
      })
    }

    const handleCheckReject = function(index, row) {
      console.log(row)
      store.dispatch("root/requestCheckInviteReject", {
        study_id: row["studyId"],
        receiver_id: row["userName"]
      })
      .then(function(res) {
        // console.log("!!!!")
        state.joinList.splice(index, 1)
        console.log('123',row)
        console.log(index, state.joinList)
      })
      .catch(function(err) {
        console.log("error",err)
      })
    }

  return { state, studyMemberList, inviteSendList, handleClick, handleCancelInvite, handleCheckReject }
  }
}
</script>

<style>
.study-membercard {
  display: inline-block;
}
</style>
