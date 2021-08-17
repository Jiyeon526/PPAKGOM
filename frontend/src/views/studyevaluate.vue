<template>
  <el-row>
    <h1>스터디원 평가</h1>
  </el-row>
  <el-row :gutter="24">
    <el-col :span="12">
      <h1>스터디원 평가</h1>
    <el-table
      :data="state.memberList"
      height="300"
      @row-click="handleClick"
      style="width: 100%">
      <el-table-column
        prop="study_id"
        label="방번호"
      >
      </el-table-column>
      <el-table-column
        prop="name"
        label="스터디명"
      >
      </el-table-column>
      <el-table-column
        prop="study_member_name"
        label="스터디원"
      >
      </el-table-column>
      <el-table-column
        prop="checked"
        align='right'
      >
      <template #default="scope">
      <!-- <div v-if="scope.row['checked']"> -->
        <i v-if="scope.row['checked']" class="el-icon-circle-check"></i>
      <!-- </div> -->
      </template>
      </el-table-column>
    </el-table>
    </el-col>
    <el-col :span=1></el-col>
    <el-col :span="10" >
      <div @click="handleclick" v-if="state.isclick">
        <evaluateDetail :memberData="state.memberData" :studyData="state.studyData"/>
      </div>
    </el-col>
  </el-row>

</template>

<script>
import { onMounted, reactive } from "vue";
import { useStore } from "vuex";
import evaluateDetail from "./evaluate-detail.vue"

export default {
  name: "Studyevaluate",
  components: {
    evaluateDetail,
  },

  setup(props, { emit }) {
    const store = useStore();

    const state = reactive({
      memberList: [],
      inStudyList: [],
      memberData: [],
      studyData: [],
      isclick: false,
    })

    onMounted(() => {
      store.commit("root/setMenuActiveMenuName", "evaluate")
      askmemberList()
    })

    const handleClick = function(row, column, cell, event) {
      state.studyData = row
      store.dispatch("root/requestNameUserJoinStudyList", row["study_member_name"])
      .then(function(res) {
        state.inStudyList = []
        state.inStudyList = res.data
        state.studyData["joined_study"] = state.inStudyList
      })
      store.dispatch("root/requestOtherProfile", row["study_member_name"])
      .then(function(res) {
        state.isclick = true
        state.memberData = []
        const profileData = res.data
        const origin_url = profileData["profile_thumbnail"]
        const need_from = origin_url.indexOf('image')
        const url_length = origin_url.length
        const process_thumbnail = origin_url.substring(need_from,url_length)
        profileData["profile_thumbnail"] = process_thumbnail
        // profileData["joined_study"] = state.inStudyList
        state.memberData = profileData
      })
      .catch(function(err) {
        console.log(err)
      })
    }

    const handleDelete = function(index, row) {
      console.log(index, row)
    }

    const askmemberList = function() {
      store.dispatch("root/requestEvaluateMemberList")
      .then(function(res){
        state.memberList = res.data
      })
    }

    return {state, handleClick, handleDelete, askmemberList }

  }
}
</script>

<style>

.el-table {
  border: solid #dcdfe6;
}
.el-icon-circle-check {
  color: green;
}
</style>
