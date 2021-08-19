<template>

    <el-row :gutter="24">
    <el-col :span="12">
      <h1>가입신청 현황</h1>
    <el-table
      :data="state.joinList"
      height="400"
      @row-click="handleClick"
      style="width: 100%">
      <template #empty>
        <h3>가입 신청한 현황이 없습니다!</h3>
      </template>
      <el-table-column
        prop="study_id"
        label="방번호"
      >
      </el-table-column>
      <el-table-column
        prop="study_name"
        label="스터디명"
      >
      </el-table-column>
      <el-table-column
        prop="owner_user_name"
        label="방장명"
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
              @click="handleJoinDelete(scope.$index, scope.row)">신청취소</el-button>
          </div>
          <div v-else>
            <el-button
              size="mini" type="danger"
              @click="handleCheckJoinReject(scope.$index, scope.row)">가입거절</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>
    </el-col>

  <el-col :span="12">
      <h1>초대 받은 현황</h1>
    <el-table
      :data="state.receiveList"
      height="400"
      @row-click="handleUserClick"
      style="width: 100%">
      <template #empty>
        <h3>초대 받은 현황이 없습니다!</h3>
      </template>
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
        label="방장명"
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
              @click="inviteAccept(scope.$index, scope.row)">승인</el-button>
            <el-button
              size="mini" type="danger"
              @click="inviteReject(scope.$index, scope.row)">거절</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>
    </el-col>
  </el-row>
</template>

<script>
import { onMounted, reactive } from "vue";
import { useStore } from "vuex";

export default {
  name: "Studymanage",
  setup(props, { emit }) {
    const store = useStore();

    const state = reactive({
      joinList: [],
      receiveList: [],
      inStudyList: [],
      profileData: [],
      studyId: 0,
      ownerId: 0,
      ownerName: "",
    })

    onMounted(() => {
      store.commit("root/setMenuActiveMenuName", "manage")
      askJoinList()
      inviteReceiveList()
    })

    const handleClick = async function(row, column, cell, event) {
      if (column.property != "state") {
        const wantOwner = await store.dispatch("root/requestNameUserJoinStudyList",row["owner_user_name"])
        state.inStudyList = []
        state.inStudyList = wantOwner.data
        store.dispatch("root/requestOtherProfile", row["owner_user_name"])
        .then(function(res) {
          const profileData = res.data
          const origin_url = profileData["profile_thumbnail"]
          const need_from = origin_url.indexOf('image')
          const url_length = origin_url.length
          const process_thumbnail = origin_url.substring(need_from,url_length)
          profileData["profile_thumbnail"] = process_thumbnail
          emit("openOtherpeopleDialog", profileData, state.inStudyList)
        })
      }
    }

    const handleUserClick = async function(row, column, cell, event) {
      if (column.property != "state") {
        const wantOwner = await store.dispatch("root/requestNameUserJoinStudyList",row["userName"])
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
      }
    }

    const handleJoinDelete = async function(index, row) {
      const wantOwner = await store.dispatch("root/requestStudyInfoDetail", row["study_id"])
      state.studyId = wantOwner.data.studyResult[0]['study_id']
      state.ownerId = wantOwner.data.studyResult[0]['owner_id']
      store.dispatch("root/requestCancelJoin", {
        study_id: state.studyId,
        user_id: state.ownerId
      })
      .then(function(res) {
        console.log("!!!!")
      })
      .catch(function(err) {
        console.log("error",err)
      })
      state.joinList.splice(index, 1)
    }

    const handleCheckJoinReject = async function(index, row) {
      const wantOwner = await store.dispatch("root/requestStudyInfoDetail", row["study_id"])
      state.studyId = wantOwner.data.studyResult[0]['study_id']
      state.ownerId = wantOwner.data.studyResult[0]['owner_id']
      store.dispatch("root/requestCheckJoinReject", {
        study_id: state.studyId,
        user_id: state.ownerId
      })
      .then(function(res) {
        console.log("!!!!")
      })
      .catch(function(err) {
        console.log("error",err)
      })
      state.joinList.splice(index, 1)
    }

    const askJoinList = function() {
      store.dispatch("root/requestAskJoinList")
      .then(function(res){
        state.joinList= res.data
      })
    }

    const inviteReceiveList = function() {
      store.dispatch("root/requestinviteReceiveList")
      .then(function(res){
        state.receiveList = res.data.inviteResult
      })
    }

    const inviteAccept = async function(index, row) {
      const wantOwner = await store.dispatch("root/requestStudyInfoDetail", row["studyId"])
      state.studyId = wantOwner.data.studyResult[0]['study_id']
      state.ownerId = wantOwner.data.studyResult[0]['owner_id']
      let body = new FormData()
      body.append("studyId",state.studyId)
      body.append("senderId",state.ownerId)
      store.dispatch("root/requestSendAccept", body)
      .then(function(res) {
        console.log("!!!!",res)
      })
      .catch(function(err) {
        console.log("error",err)
      })
      state.receiveList.splice(index, 1)
    }

    const inviteReject = async function(index, row) {
      const wantOwner = await store.dispatch("root/requestStudyInfoDetail", row["studyId"])
      state.studyId = wantOwner.data.studyResult[0]['study_id']
      state.ownerId = wantOwner.data.studyResult[0]['owner_id']
      let body = new FormData()
      body.append("studyId",state.studyId)
      body.append("senderId",state.ownerId)
      store.dispatch("root/requestSendReject", body)
      .then(function(res) {
        })
      state.receiveList.splice(index, 1)
    }

    return {state, handleClick, handleUserClick, handleJoinDelete, askJoinList, inviteReceiveList, inviteAccept, inviteReject, handleCheckJoinReject }

  }
}
</script>

<style>

.el-table {
  border: solid #dcdfe6;
}

</style>
