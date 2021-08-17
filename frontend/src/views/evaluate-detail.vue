<template>
  <div class="evaluate-box">
    <el-row :gutter="24">
      <el-col :span="16">
        <p>{{memberData.name}}</p>
        <p>{{memberData.temperature}}</p>
        <el-progress :stroke-width="20" :percentage="memberData.temperature" :show-text='false'>
        </el-progress>
      </el-col>
      <el-col :span="8">
        <el-image style="width: 100px; height: 100px"
        :src="'https://localhost:8443/' + memberData.profile_thumbnail"
        :fit="fit"
        >
        </el-image>
      </el-col>
    </el-row>
    <el-divider></el-divider>
    <el-row>
      <el-col>
        <p>가입된 스터디</p>
        <li v-for="std in studyData.joined_study">
          {{ std }}
        </li>
      </el-col>
      <el-col>
        <el-rate
          style="display: inline-block"
          :icon-classes="state.iconClasses"
          v-model="state.rating"></el-rate>
        <el-button v-if="studyData.checked == false" style="display: inline-block" @click="handleEvaluate">평가</el-button>
        <el-button v-else style="display: inline-block">평가완료</el-button>
      </el-col>
      <el-col>
        <span>관심분야: </span>
        <span v-for="interest in memberData.interest">#{{interest}} </span>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { onMounted, reactive, ref } from "vue";
import { useStore } from "vuex";

export default {
  name: "evaluate-detail",

  props: {
    // open: {
    //   type: Boolean,
    //   default: false
    // },
    memberData: {
      type: Object,
    },
    studyData: {
      type: Object,
    }
  },
  setup(props, { emit }) {
    const store = useStore();

    const state = reactive({
      formLabelWidth: "120px",
      rating: null,
      passion: 50,
      iconClasses: ['el-icon-star-on', 'el-icon-star-on','el-icon-star-on'],
      circleUrl: "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
    });

    onMounted(() => {
    });

    const handleClick = function(row, column, cell, event) {
      console.log(row,column,cell,event)
    }

    const handleDelete = function(index, row) {
      console.log(index, row)
    }

    const handleEvaluate = function() {
      let body = new FormData();
      body.append("rating", state.rating)
      body.append("studyId", props.studyData["study_id"])
      body.append("studyMemberId", props.studyData["studyMemberId"])
      store.dispatch('root/requestSendMemberRating',body)
      .then(function(res) {
        console.log(res)
      })
      .catch(function(err) {
        console.log(err)
      })
    }

    return {state, handleClick, handleDelete, handleEvaluate }

  }
}
</script>

<style>
.evaluate-box {
  height: 500px;
  border: solid;
}
.el-rate__icon {
  font-size: 1.5rem;
}
</style>
