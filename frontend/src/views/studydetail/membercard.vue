<template>
  <el-card :body-style="{ padding: '0px' }">
    <div class="image-wrapper">
      <el-image style="width: 100%; height: 200px"
        :src="state.uri"
        :fit="fill">
      </el-image>
      <!-- <el-image style="width: 100%; height: 200px"
        :src="'https://localhost:8443/' + memberData.thumbnail"
        :fit="fill">
      </el-image> -->
    </div>
    <div style="text-align: left; padding: 14px;">
      <p>{{memberData}}</p>
      <span v-if="condition == 1">
        {{memberData.user_name}}님
        <el-button @click="inviteMember" >초대</el-button>
        <!-- <div v-if="!isclick" >
          <el-button @click="inviteMember" >초대</el-button>
        </div>
        <div v-else>
          <el-button @click="inviteMember" disabled>초대중</el-button>
        </div> -->
      </span>
      <span v-else>{{memberData.name}}님</span>
    </div>
  </el-card>
</template>

<style scoped>
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

</style>

<script>
import { onMounted, reactive, watch } from 'vue'
import { useStore } from 'vuex'
import { ElMessage } from "element-plus"
import axios from "axios"

export default {
  name: 'membercard',
  props: {
    memberData: {
      type: Object
    },
    condition: {
      type: Number,
      default: 0
    }
  },

  setup (props, {emit} ) {
    const store = useStore()
    const state = reactive({
      uri: "",
      studyData: "",
      isclick: false,
    })

    // watch(()=>state.isclick)


    // 페이지 진입시 불리는 훅
    onMounted (() => {
      state.studyData = props.memberData.thumbnail;

      var name;
      if (!state.studyData) {
        state.studyData = "default.png/default.png/default.png"
      }
      if (
        state.studyData.split("\\").length > state.studyData.split("/").length
      ) {
        name = state.studyData.split("\\");
      } else {
        name = state.studyData.split("/");
      }

      axios({
        url: `https://localhost:8443/api/v1/users/profile/${name[2]}/download`,
        method: "GET",
        responseType: "blob"
      }).then(res => {
        state.uri = URL.createObjectURL(res.data);
      })
    })

    const inviteMember = function() {
      const body = new FormData()
      body.append("receiver_id",props.memberData["id"])
      body.append("is_join",false)
      store.dispatch('root/requestInviteMember', body)
      .then(function(res) {
        ElMessage({
          message: "초대완료",
          type: "success"
        })
        state.isclick = true
        store.dispatch("root/requestinviteSendList")
        .then(function(res) {
          store.commit('root/setSendInviteMemberList',res.data.inviteResult)
        })
      })
      .catch(function(err) {
        console.log(err)
      })
    }

    const handleClose = function() {
      state.isclick = false
    };

  return { state, inviteMember, handleClose }
  }
}
</script>
