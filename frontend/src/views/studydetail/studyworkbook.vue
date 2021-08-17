<template>
  <h2>스터디 문제집</h2>

  <el-button @click="clickCreateWorkbook">문제집 만들기</el-button>
  <el-row :gutter="24" justify="center">
    <el-col :span="12">
      <h1>가입된 스터디</h1>
    <el-table
      :data="state.workbookList"
      height="400"
      @row-click="clickWorkbook"
      style="width: 100%">
      <el-table-column
        prop="date"
        label="Date"
      >
      </el-table-column>
      <el-table-column
        prop="id"
        label="NO."
      >
      </el-table-column>
      <el-table-column
        prop="name"
        label="생성자"
      >
      </el-table-column>
      <el-table-column
        prop="title"
        label="문제집 이름"
      >
      </el-table-column>
    </el-table>
    </el-col>
  </el-row>


</template>

<script>
import { onMounted, reactive } from 'vue'
import { useStore } from 'vuex'

export default {
  name: 'studyworkbook',

  setup (props, {emit}) {
    const store = useStore()
    const state = reactive({
      workbookList: [],
    })
    // 페이지 진입시 불리는 훅
    onMounted (() => {
      AskWorkbookList()
    })

    const clickCreateWorkbook = function() {
      emit("OpenAnswerworkbookDialog")
    }

    const clickWorkbook = function(row,column,event) {
      console.log(row)
      store.dispatch('root/requestWorkbookInfo',row["id"])
      .then(function(res) {
        console.log(res.data.answer.length)
        emit("OpenMakeworkbookDialog", res.data.test, res.data.answer.length)
      })
      .catch(function(err) {
        console.log(err)
      })
    }

    const AskWorkbookList = function() {
      store.dispatch('root/requestWorkbookList')
      .then(function(res) {
        state.workbookList = res.data
      })
      .catch(function(err) {
        console.log(err)
      })
    }

  return { state, clickCreateWorkbook, clickWorkbook, AskWorkbookList }
  }
}
</script>
