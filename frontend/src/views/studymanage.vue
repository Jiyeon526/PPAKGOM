<template>
  <el-row>
    <h1>방관리</h1>
  </el-row>

    <el-row :gutter="24">
    <el-col :span="12">
      <h1>가입신청 현황</h1>
    <el-table
      :data="state.joinList"
      height="400"
      @row-click="handleClick"
      style="width: 100%">
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
              @click="handleJoinDelete(scope.$index, scope.row)">가입거절</el-button>
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
      :show-header="false"
      @row-click="handleClick"
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
              @click="handleJoinDelete(scope.$index, scope.row)">승인</el-button>
            <el-button
              size="mini" type="danger"
              @click="handleJoinDelete(scope.$index, scope.row)">거절</el-button>
          </div>
          <div v-else>
            1234
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
    })

    onMounted(() => {
      store.commit("root/setMenuActiveMenuName", "manage")
      askJoinList()
      inviteReceiveList()
    })

    const handleClick = function(row, column, cell, event) {
      if (column.property != "state") {
        // console.log(row,column,cell)
        // const ownerName = row["owner_user_name"]
        // 여기 api 변경 요청
        store.dispatch("root/requestJoinStudyList")
        .then(function(res) {
          state.inStudyList = []
          // state.inStudyList = res.data.studyResult
          // const tot = res.data.studyResult.length
          for (const val in res.data.studyResult) {
            state.inStudyList.push(res.data.studyResult[val]["name"])
          }
        })
        store.dispatch("root/requestOtherProfile", row["owner_user_name"])
        .then(function(res) {
          const profileData = res.data
          const origin_url = profileData["profile_thumbnail"]
          const need_from = origin_url.indexOf('image')
          const url_length = origin_url.length
          const process_thumbnail = origin_url.substring(need_from,url_length)
          profileData["profile_thumbnail"] = process_thumbnail
          profileData["joined_study"] = state.inStudyList
          emit("openOtherpeopleDialog", profileData)
        })
        .catch(function(err) {
          console.log(err)
        })
      }
    }

    const handleJoinDelete = function(index, row) {
      store.dispatch("root/requestStudyInfoDetail", row["study_id"])
      .then(function(res) {
        const studyId = res.data.studyResult[0]['study_id']
        const ownerId = res.data.studyResult[0]['owner_id']
        store.dispatch("root/requestCancelJoin", {
          study_id: studyId,
          user_id: ownerId
        })
        .then(function(res) {
          // console.log("!!!!")
        })
        .catch(function(err) {
          console.log("error",err)
        })
      })
      .catch(function(err) {
        console.log("error",err)
      })
      state.joinList.splice(index, 1)
      console.log('123',row)
      console.log(index, state.joinList)
    }

    const askJoinList = function() {
      store.dispatch("root/requestAskJoinList")
      .then(function(res){
        state.joinList= res.data
        console.log("1234",state.joinList)
      })
    }

    const inviteReceiveList = function() {
      // 이거 userName 주는 거 방장이 맞나?
      store.dispatch("root/requestinviteReceiveList")
      .then(function(res){
        state.receiveList = res.data.inviteResult
        console.log("1234",state.receiveList)
      })
    }

    return {state, handleClick, handleJoinDelete, askJoinList, inviteReceiveList }

  }
}
</script>

<style>

.el-table {
  border: solid #dcdfe6;
}

</style>
