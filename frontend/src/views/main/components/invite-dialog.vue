<template>
  <el-dialog
    custom-class="invite-dialog"
    title="초대하기"
    v-model="state.dialogVisible"
    @close="handleClose"
  >
    <div class="search-member">
      <el-input
        placeholder="관심사 검색"
        v-model="state.searchValue"
        @keyup.enter="searchMember"
      >
        <template #append>
          <el-button icon="el-icon-search" @click="searchMember"></el-button>
        </template>
      </el-input>
    </div>
    <div v-if="state.memberList.length !== 0" style="display: flex; justify-content: center; flex-wrap: wrap; ">
      <div
        style="display: inline-block;"
        v-for="i in state.memberList.length"
        :key="i"
      >
        <membercard  :memberData="state.memberList[i-1]" :condition="1" />
      </div>
    </div>
    <el-alert
      v-else
      title="존재하는 스터디가 없습니다. 새롭게 스터디를 생성하시거나 다른 제목으로 스터디를 검색해주세요."
      type="error"
      center
    >
    </el-alert>
  </el-dialog>
</template>
<script>
import { reactive, computed, ref, onMounted } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import membercard from '../../studydetail/membercard.vue';
export default {
  components: { membercard },
  name: "invite-dialog",

  props: {
    open: {
      type: Boolean,
      default: false
    }
  },

  setup(props, { emit }) {
    const store = useStore();
    const router = useRouter();

    const state = reactive({
      searchValue: "",
      memberList: [],
      dialogVisible: computed(() => props.open),
      formLabelWidth: "120px"
    });

    const isDisabled = function() {
      return "disabled";
    };

    onMounted(() => {
      // console.log(loginForm.value)
    });

    // 검색한 내용으로 스터디 목록 가져오기
    const searchMember = function() {
      let cleanValue = state.searchValue.trim();
      console.log(cleanValue)
      if (cleanValue !== "") {
        store
          .dispatch("root/requestSearchInterest", cleanValue)
          .then(function(res) {
            for ( const val in res.data) {
            const profileData = res.data[val]
            const origin_url = profileData["thumbnail"]
            const need_from = origin_url.indexOf('image')
            const url_length = origin_url.length
            const process_thumbnail = origin_url.substring(need_from,url_length)
            res.data[val]["thumbnail"] = process_thumbnail
            }
            state.memberList = res.data
          })
          .catch(function(err) {
            console.log("검색 에러", err)
          })
      } else {
        state.searchValue = ""
      }
    }

    const handleClose = function() {
      emit("closeInviteDialog");
    };

    return { state, handleClose, searchMember }
  }
};
</script>
