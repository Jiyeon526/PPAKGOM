<template>
  <h2>스터디 문제집</h2>

  <el-button @click="clickCreateWorkbook">문제집 만들기</el-button>
  <el-button @click="clickWorkbook">문제집 확인</el-button>
  <el-row :gutter="24" justify="center">
    <el-col :span="12">
      <h1>가입된 스터디</h1>
    <el-table
      :data="state.tableData"
      height="400"
      :show-header="false"
      @row-click="handleClick"
      style="width: 100%">
      <el-table-column
        prop="date"
        label="Date"
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
      count: 10,
      val: '',
      tableData: [{
          date: '2016-05-03',
        }, {
          date: '2016-05-02',
        }, {
          date: '2016-05-04',
        }, {
          date: '2016-05-08',
        }, {
          date: '2016-05-06',
        }, {
          date: '2016-05-07',
        }],
    })
    // 페이지 진입시 불리는 훅
    onMounted (() => {

    })

    const clickCreateWorkbook = function() {
      emit("OpenAnswerworkbookDialog")
    }

    const clickWorkbook = function() {
      emit("OpenMakeworkbookDialog")
    }

  return { state, clickCreateWorkbook, clickWorkbook }
  }
}
</script>
