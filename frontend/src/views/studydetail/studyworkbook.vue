<template>
  <div>
    <h2>스터디 문제집</h2>
    <el-row :gutter="24" justify="center">
      <el-col :span="12">
      <el-table
        :data="state.workbookList"
        height="400"
        @row-click="clickWorkbook"
        style="width: 100%">
        <template #empty>
          <h3>문제집을 만들고 공유해보세요!</h3>
        </template>
        <el-table-column
          type="index"
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
      <el-button style="margin: 10px 0px 10px 10px; display: block; float: right;" plain type="success" @click="clickCreateWorkbook">문제집 만들기</el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { computed, onMounted, reactive } from 'vue'
import { useStore } from 'vuex'

export default {
  name: 'studyworkbook',

  setup (props, {emit}) {
    const store = useStore()
    const state = reactive({
      workbookList: computed(() => store.getters['root/getWorkbookList']),
    })

    onMounted (() => {
      AskWorkbookList()
    })

    const clickCreateWorkbook = function() {
      emit("OpenAnswerworkbookDialog")
    }

    const clickWorkbook = function(row,column,event) {
      store.dispatch('root/requestWorkbookInfo',row["id"])
      .then(function(res) {
        emit("OpenMakeworkbookDialog", res.data.test, res.data.answer.length)
      })
      .catch(function(err) {
        console.log(err)
      })
    }

    const AskWorkbookList = function() {
      store.dispatch('root/requestWorkbookList')
      .then(function(res) {
        store.commit('root/setWorkbookList', res.data)
      })
      .catch(function(err) {
        console.log(err)
      })
    }

  return { state, clickCreateWorkbook, clickWorkbook, AskWorkbookList }
  }
}
</script>
