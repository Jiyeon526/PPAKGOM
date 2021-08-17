<template>
  <el-card :body-style="{ padding: '0px' }">
    <div class="image-wrapper">
      <el-image
        style="width: 100%; height: 250px;"
        :src="state.uri"
        :fit="fit"
      >
      </el-image>
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
        <el-tag
          v-if="studyData.enter"
          type="success"
          @click.stop="enterStudy(studyData.study_id)"
          >입장</el-tag
        >
      </el-input-data>
    </div>
  </el-card>
</template>
<style scoped>
.el-card {
  width: 300px;
  height: auto;
  margin-bottom: 40px;
  border-radius: 10px;
  border-color: rgb(143, 209, 141);
}
.el-card:hover {
  transform: scale(1.05, 1.05);
  box-shadow: 5px 5px 5px rgb(143, 209, 141, 0.7);
}
.el-card .image-wrapper {
  width: 300px;
  height: auto;
  margin: 0px;
  text-align: center;
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
  margin: 4px;
}
</style>
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
      uri: '',
      studyData: "",
    });

    const enterStudy = studyPk => {
      console.log("스터디 데이터", studyPk);
      localStorage.setItem("studypk", studyPk);
      store.commit("root/setStudypk", studyPk);
      store.commit("root/setSelectOption", "studyhome");
      router.push({
        name: "studyhome"
      });
    };

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
        url: `https://localhost:8443/api/v1/study/${name[2]}/download`,
        method: "GET",
        responseType: "blob"
      }).then(res => {
        state.uri = URL.createObjectURL(res.data);
      });
    });

    return { state, enterStudy };
  }
};
</script>
