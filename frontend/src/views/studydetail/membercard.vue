<template>
  <el-card :body-style="{ padding: '0px' }">
    <div class="image-wrapper">
      <el-image
        style="width: 100%; height: 200px"
        :src="'https://localhost:8443/' + memberData.thumbnail"
        :fit="fit"
      >
      </el-image>
    </div>
    <div style="text-align: left; padding: 14px;">
      <p>{{ memberData }}</p>
      <span v-if="condition == 1">
        {{ memberData.user_name }}님
        <el-button @click="inviteMember">초대</el-button>
      </span>
      <span v-else>{{ memberData.name }}님</span>
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
import { onMounted, reactive } from "vue";
import { useStore } from "vuex";

export default {
  name: "membercard",
  props: {
    memberData: {
      type: Object
    },
    condition: {
      type: Number,
      default: 0
    }
  },

  setup(props, { emit }) {
    const store = useStore();
    const state = reactive({});

    // 페이지 진입시 불리는 훅
    onMounted(() => {});

    const inviteMember = function() {
      const body = new FormData();
      body.append("receiver_id", props.memberData["id"]);
      body.append("is_join", false);
      store
        .dispatch("root/requestInviteMember", body)
        .then(function(res) {
          console.log(res);
        })
        .catch(function(err) {
          console.log(err);
        });
    };

    return { state, inviteMember };
  }
};
</script>
