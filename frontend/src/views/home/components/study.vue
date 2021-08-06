<template>
  <el-card :body-style="{ padding: '0px' }">
    <div class="image-wrapper">
      <el-image style="width: 100%; height: 190px"
        :src="'https://localhost:8443/' + studyData.study_thumbnail"
        :fit="fit"
        >
      </el-image>
    </div>
    <div style="text-align: left; padding: 14px;">
      <span class="title">{{ studyData.name }}</span>
      <div class="bottom">
        <span>{{ studyData.content }}</span>
      </div>
    </div>
    <div>
      <el-input-data class="btn-sort">
        <el-tag type=""> {{ studyData.joined_population }}/{{ studyData.population }}</el-tag>
      </el-input-data>
    </div>
  </el-card>
</template>
<style>
.el-card {
  margin: 0 8px;
  margin-bottom: 40px;
}
.el-card .image-wrapper {
  width: 100%;
  height: 190px;
}
.el-card .title {
  width: 345px;
  display:-webkit-box;
  font-weight: bold;
  word-wrap:break-word;
  -webkit-box-orient:vertical;
  overflow:hidden;
  text-overflow:ellipsis;
  -webkit-line-clamp: 1;
}
.el-card .bottom {
  margin-top: 5px;
  display:-webkit-box;
  word-wrap:break-word;
  -webkit-box-orient:vertical;
  overflow:hidden;
  text-overflow:ellipsis;
}

/* 테블릿, 모바일의 경우 두 줄 말줄임표시 */
@media (max-width: 1269px) {
  .el-card .bottom {
    -webkit-line-clamp: 2;
    height:42px;
  }
}
/* 데스크탑의 경우 세 줄 말줄임표시 */
@media (min-width: 1270px) {
  .el-card .bottom {
    -webkit-line-clamp: 3;
    height:60px;
  }
}

/* 인원 왼쪽 정렬 */
.btn-sort {
  display: flex;
  justify-content: left;
}

</style>
<script>
import { onMounted, reactive } from 'vue'


export default {
  name: 'Home',

  props: {
    studyData: {
      type: Object,
    }
  },

  setup () {
    const state = reactive({

  })



    return { state, }
  }
}
</script>
