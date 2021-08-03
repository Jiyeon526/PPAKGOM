<template>
  <el-card :body-style="{ padding: '0px' }">
    <div class="image-wrapper">
      <el-image style="width: 100%; height: 190px"
        :src="'https://localhost:8443/' + conferenceData.thumbnail_url"
        :fit="fit"
        >
      </el-image>
    </div>
    <div style="text-align: left; padding: 14px;">
      <span class="title">{{ conferenceData.title }}</span>
      <div class="bottom">
        <span>{{ conferenceData.description }}</span>
      </div>
    </div>
    <div>
      <el-input-data class="btn-sort">
        <el-tag v-if="state.isMeeting" class="btn-m" type="success" effect="plain">진행중</el-tag>
        <el-tag v-else class="btn-m" type="danger" effect="plain">종료됨</el-tag>
        <el-tag class="btn-m" type="">{{ conferenceData.joinUsersNum }}명 참여중</el-tag>
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

/* 버튼 양 사이드로 정렬 */
.btn-sort {
  display: flex;
  justify-content: space-between;
}

.btn-m {
  margin: 8px 14px;
}

</style>
<script>
import { onMounted, reactive } from 'vue'


export default {
  name: 'Home',

  props: {
    title: {
      type: String,
      default: '제목'
    },
    desc: {
      type: String,
      default: "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."
    },
    conferenceData: {
      type: Object,
    }
  },

  setup () {
    const state = reactive({
      isMeeting: false,
      }
    )

    // 받아오는 상태(진행중/종료됨)에 따른 isMeeting toggle
    const meetingState = function () {
      state.isMeeting = !state.isMeeting
    }

    return { state, meetingState }
  }
}
</script>
