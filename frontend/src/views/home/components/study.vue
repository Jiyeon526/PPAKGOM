<template>
  <el-card :body-style="{ padding: '0px' }">
    <div class="image-wrapper">
      <!--  :src="'https://i5b306.p.ssafy.io/' + studyData.study_thumbnail" -->
      <el-image :src="state.uri" style="width: 100%; height: 200px" />
    </div>
    <div style="text-align: left; padding: 14px;">
      <span class="title"
        >{{ studyData.study_id }}번방 {{ studyData.name }}</span
      >
      <div class="bottom">
        <span>{{ studyData.content }}</span>
      </div>
    </div>
    <div>
      <el-input-data class="btn-sort">
        <el-tag type="">
          {{ studyData.joined_population }}/{{ studyData.population }}</el-tag
        >
        <el-tag v-if="studyData.enter" type="success" @click="enterStudy"
          >입장</el-tag
        >
      </el-input-data>
    </div>
  </el-card>
</template>

<script>
import { onMounted, reactive } from "vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex";
import axios from "axios";

export default {
  name: "Home",

  props: {
    studyData: {
      type: Object
    }
  },

  setup(props, { emit }) {
    const store = useStore();
    const router = useRouter();
    const state = reactive({
      studyData: "",
      uri: ""
    });
    onMounted(() => {
      state.studyData = props.studyData.study_thumbnail;
      console.log(state.studyData);
      var name;
      if (
        state.studyData.split("\\").length > state.studyData.split("/").length
      ) {
        name = state.studyData.split("\\");
      } else {
        name = state.studyData.split("/");
      }

      console.log(name);
      //name = "9-kakao.jpg";
      axios({
        url: `https://i5b306.p.ssafy.io/api/v1/study/${name[2]}/download`,
        method: "GET",
        responseType: "blob"
      }).then(res => {
        state.uri = URL.createObjectURL(res.data);
      });
    });
    const enterStudy = studyData => {
      store.commit("root/setStudypk", studyData.study_id);
      store.commit("root/setSelectOption", "studyhome");
      router.push({
        name: "studyhome"
      });
    };

    return { state, enterStudy };
  }
};
</script>
<style>
.el-card {
  width: 300px;
  height: auto;
  margin: 0 8px;
  margin-bottom: 40px;
}
.el-card .image-wrapper {
  width: 100%;
  height: 200px;
}
.el-card .title {
  display: -webkit-box;
  font-weight: bold;
  word-wrap: break-word;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  -webkit-line-clamp: 1;
}
.el-card .bottom {
  margin-top: 5px;
  display: -webkit-box;
  word-wrap: break-word;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 테블릿, 모바일의 경우 두 줄 말줄임표시 */
@media (max-width: 1269px) {
  .el-card .bottom {
    -webkit-line-clamp: 2;
    height: 42px;
  }
}
/* 데스크탑의 경우 세 줄 말줄임표시 */
@media (min-width: 1270px) {
  .el-card .bottom {
    -webkit-line-clamp: 3;
    height: 60px;
  }
}

/* 인원 왼쪽 정렬 */
.btn-sort {
  display: flex;
  justify-content: space-between;
  margin: 2px;
}
</style>
