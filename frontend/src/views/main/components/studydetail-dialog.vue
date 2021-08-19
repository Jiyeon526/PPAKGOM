<template>
  <el-dialog
    width="40%"
    :title="selectStudy.study_id + '번 스터디'"
    v-model="state.dialogVisible"
    @close="handleClose"
  >
    <!-- <div v-if="!selectStudy.enter" style="display:flex; justify-content:flex-end" >
      <el-button v-if="state.likeStudy" style="border:0; outline:0" icon="el-icon-star-on" @click="clickLikeCancleBtn"></el-button>
      <el-button v-else style="border:0; outline:0" icon="el-icon-star-off" @click="clickLikeBtn"></el-button>
    </div> -->
    <el-divider style="margin: 5px"></el-divider>
    <div style="display:flex; align-items:center">
      <el-image style="width: 50%; height: 50%;"
        :src="state.uri"
        :fit="fit"
        >
      </el-image>
      <div style="width:50%; height:50%; text-align:center">
        <h2 style="margin:30px">{{ selectStudy.name }}
          <i v-if="state.likeStudy" @click="clickLikeCancleBtn" class="fas fa-heart" style="color:red"></i>
          <i v-else @click="clickLikeBtn" class="far fa-heart" style="color:red"></i>
        </h2>
        <h4>모집 인원 : {{ selectStudy.joined_population }}/{{ selectStudy.population }}</h4>
        <h4>관심 분야 : {{ state.interested }}</h4>
        <h4>마감 날짜 : {{ selectStudy.deadline }}</h4>
      </div>
    </div>
    <div>
      <h3>설명</h3>
      <p>{{ selectStudy.content }}</p>
    </div>
    <div>
      <h3 style="margin-bottom:5px;">열정도</h3>
        <p style="text-align:right; font-weight:bold; margin-top:2px; margin-bottom:2px;">{{ selectStudy.temperature }}℃
          <i v-if="state.iconTemp === 1" class="far fa-angry"></i>
          <i v-if="state.iconTemp === 2" class="far fa-laugh"></i>
          <i v-if="state.iconTemp === 3" class="far fa-laugh-squint"></i>
        </p>
        <el-progress show-text="false" :stroke-width="24" :percentage="selectStudy.temperature" status="success" :color="state.customColor"></el-progress>
    </div>
    <!-- <div v-if="!selectStudy.enter" style="display:flex; justify-content:flex-end" >
      <el-button v-if="state.likeStudy" style="border:0; outline:0" @click="clickLikeCancleBtn"><i class="fas fa-heart" style="color:red"></i></el-button>
      <el-button v-else style="border:0; outline:0" @click="clickLikeBtn"><i class="far fa-heart" style="color:red"></i></el-button>
    </div> -->
    <el-divider style="margin: 10px 0px"></el-divider>
    <div style="display:flex; flex-direction:column; justify-content:center; align-items:center">
      <div>
        <Studyschedulecomponent :studyId="state.studyId" />
      </div>
    </div>
    <el-divider style="margin: 5px"></el-divider>
    <div class="detail-dialog-footer">
      <el-button v-if="selectStudy.enter" type="success" plain style="height: 30px" @click="enterStudy">입장</el-button>
      <el-button v-else type="success" plain style="height: 30px" @click="requestJoinStudy">가입</el-button>
    </div>
  </el-dialog>
</template>
<style scoped>
.el-dialog__header {
  padding: 20px !important
}
.el-dialog__body {
  padding: 5px 20px !important
}
.detail-dialog-footer {
  display: flex;
  justify-content: flex-end;
  align-items: center;
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
  justify-content: space-between;
  margin: 2px;
}
.el-progress__text {
  display: none;
  width: 0px;
}

</style>
<script>
import Studyschedulecomponent from "@/views/studydetail/studyschedulecomponent"
import { reactive, computed, onMounted, watch } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import axios from "axios";

export default {
  name: "studydetail-dialog",
  components: {
    Studyschedulecomponent,
  },

  props: {
    open: {
      type: Boolean,
      default: false
    },
    selectStudy: {
      type: Object,
    }
  },

  setup(props, { emit }) {
    const store = useStore();
    // 마운드 이후 바인딩 될 예정 - 컨텍스트에 노출시켜야함. <return>
    const router = useRouter();
    /*
      // Element UI Validator
      // rules의 객체 키 값과 form의 객체 키 값이 같아야 매칭되어 적용됨
      //
    */
    const state = reactive({
      dialogVisible: computed(() => props.open),
      firstLikeInfo: computed(() => props.selectStudy.liked),
      likeStudy: false,
      studyId: computed(() => props.selectStudy.study_id),
      interested: computed(() => props.selectStudy.interest.join(', ')),
      studyMember: [],
      customColor: '',
      iconTemp: 2,
      studyData: '',
      uri: '',
    });

    watch(
      () => state.studyId,
      () => {
        state.likeStudy = state.firstLikeInfo
        if (props.selectStudy.temperature <= 20) {
          state.customColor = "#f56c6c"
          state.iconTemp = 1
        }
        else if (35 <= props.selectStudy.temperature && props.selectStudy.temperature <= 45) {
          state.customColor = "#e6a23c"
          state.iconTemp = 2
        }
        else if (45 <= props.selectStudy.temperature && props.selectStudy.temperature <= 60) {
          state.customColor = "#5cb87a"
          state.iconTemp = 2
        }
        else if (60 <= props.selectStudy.temperature && props.selectStudy.temperature <= 80) {
          state.customColor = "#1989fa"
          state.iconTemp = 3
        }
        else {
          state.customColor = "#6f7ad3"
          state.iconTemp = 3
        }
      }
    )

    watch(
      () => props.selectStudy,
      () => {
        // 이미지 불러오기
        state.studyData = props.selectStudy.study_thumbnail;
        if (!state.studyData) {
          state.studyData = "default.png/default.png/default.png"
        }
        var name;
        if (
          state.studyData.split("\\").length > state.studyData.split("/").length
        ) {
          name = state.studyData.split("\\");
        } else {
          name = state.studyData.split("/");
        }
        axios({
          url: `https://localhost:8443/api/v1/study/${name[2]}/download`,
          method: "GET",
          responseType: "blob"
        }).then(res => {
          state.uri = URL.createObjectURL(res.data);
        });
    })

    onMounted(() => {

    });

    const enterStudy = () => {
    store.commit("root/setStudypk", props.selectStudy.study_id);
    store.commit("root/setSelectOption", "studyhome");
    handleClose()
    router.push({
      name: 'studyhome'
    })
  }

    const requestJoinStudy = () => {
      store
        .dispatch('root/requestJoinStudy', {
          studyId: props.selectStudy.study_id
        })
          .then(function(res) {
            ElMessage({
              type: "success",
              message: "가입 요청을 보냈습니다."
            })
          })
          .catch(function(err) {
            ElMessage({
              type: "error",
              message: err.message
            })
          })
    }

    const clickLikeBtn = () => {
      state.likeStudy = !state.likeStudy
      store
        .dispatch('root/requestLikeStudy', {
          studyId: props.selectStudy.study_id
        })
          .then(function(res) {
            ElMessage({
              type: "success",
              message: `${props.selectStudy.name} 스터디를 찜하였습니다.`
            })
          })
          .catch(function(err) {
              ElMessage({
              type: "error",
              message: err.message
            })
          })
    }

    const clickLikeCancleBtn = () => {
      state.likeStudy = !state.likeStudy
      store
        .dispatch('root/requestLikeCancleStudy', {
          studyId: props.selectStudy.study_id
        })
          .then(function(res) {
            ElMessage({
              type: "success",
              message: `${props.selectStudy.name} 스터디를 찜하기를 취소하였습니다.`
            })
          })
          .catch(function(err) {
            ElMessage({
              type: "error",
              message: err.message
            })
          })
    }

    const handleClose = function() {
      emit("closeStudydetailDialog");
    };

    return { state, handleClose, enterStudy, requestJoinStudy, clickLikeBtn, clickLikeCancleBtn };
  }
};
</script>
