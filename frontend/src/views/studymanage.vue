<template>
  <el-row>
    <h1>방관리</h1>
  </el-row>
  <el-row :gutter="24">
    <el-col :span="12">
      <h1>가입된 스터디</h1>
    <el-table
      :data="state.tableData"
      height="200"
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

    <el-col :span="12">
      <h1>스터디원 평가</h1>
    <el-table
      :data="state.table2Data"
      height="200"
      :show-header="false"
      @row-click="handleClick"
      style="width: 100%">
      <el-table-column
        prop="date"
        label="Date"
      >
      </el-table-column>
      <el-table-column
        label="buttons"
        align='right'
      >
        <template #default="scope">
          <el-button
            size="mini" type="primary"
            @click="handleEdit(scope.$index, scope.row)">평가</el-button>
        </template>
      </el-table-column>
    </el-table>
    </el-col>
  </el-row>
  <el-divider></el-divider>
    <el-row :gutter="24">
    <el-col :span="12">
      <h1>가입신청 현황</h1>
    <el-table
      :data="state.tableData"
      height="200"
      :show-header="false"
      @row-click="handleClick"
      style="width: 100%">
      <el-table-column
        prop="date"
        label="Date"
      >
      </el-table-column>
      <el-table-column
        label="buttons"
        align='right'
      >
        <template #default="scope">
          <el-button
            size="mini" type="warning"
            @click="handleEdit(scope.$index, scope.row)">신청취소</el-button>
        </template>
      </el-table-column>
    </el-table>
    </el-col>

  <el-col :span="12">
      <h1>가입 요청 현황</h1>
    <el-table
      :data="state.table2Data"
      height="200"
      :show-header="false"
      @row-click="handleClick"
      style="width: 100%">
      <el-table-column
        prop="date"
        label="Date"
      >
      </el-table-column>
      <el-table-column
        label="buttons"
        align='right'
      >
        <template #default="scope">
          <el-button
            size="mini" type="primary"
            @click="handleEdit(scope.$index, scope.row)">승인</el-button>
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(scope.$index, scope.row)">거절</el-button>
        </template>
      </el-table-column>
    </el-table>
    </el-col>
  </el-row>
  <el-divider></el-divider>
</template>

<script>
import { onMounted, reactive } from "vue";
import { useStore } from "vuex";

export default {
  name: "Studymanage",
  setup(props, { emit }) {
    const store = useStore();

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
        table2Data: [{
          date: '2016-05-03',
        }, {
          date: '2016-05-02',
        }, {
          date: '2016-05-04',
        }, {
          date: '2016-05-08',
        }, {
          date: '2016-05-07',
        }],
    })

    onMounted(() => {
      store.commit("root/setMenuActiveMenuName", "manage")
    })

    const handleClick = function(row, column, cell, event) {
      console.log(row,column,cell,event)
      if (column.label != "buttons") {
        console.log('!!!')
      }
    }

    const handleEdit = function(index, row) {
      console.log(index, row)
    }

    const handleDelete = function(index, row) {
      console.log(index, row)
    }

    // const askJoinList = function() {
    //   store.dispatch()
    // }

    return {state, handleClick, handleEdit, handleDelete }

  }
}
</script>

<style>

.el-table {
  border: solid #dcdfe6;
}

</style>
