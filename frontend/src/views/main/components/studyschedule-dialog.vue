<template>
  <el-dialog
    custom-class="studyschedule-dialog"
    title="스터디 일정 추가"
    v-model="state.dialogVisible"
    @close="handleClose"
  >
    <div>
      <DatePicker
        style="width:700px"
        v-model="state.date"
        mode="dateTime"
        timezone=""
      />
    <el-form ref="form" :model="state.form" label-width="120px" style="margin-top:25px; width:700px;">
      <el-form-item label="스터디 일정" >
        <el-input v-model="state.form.title"></el-input>
      </el-form-item>
      <el-form-item label="스터디 내용">
        <el-input type="textarea" v-model="state.form.content"></el-input>
      </el-form-item>
      <el-form-item style="float:right;">
        <el-button type="success">추가</el-button>
      </el-form-item>
    </el-form>
    </div>
  </el-dialog>
</template>
<script>
import { Calendar, DatePicker } from 'v-calendar';
import { reactive, computed, ref, onMounted } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";

export default {
  name: "studyschedule-dialog",
  components: {
    Calendar,
    DatePicker,
  },

  props: {
    open: {
      type: Boolean,
      default: false
    },
    // selectDate: {
    //   type: Object,
    // }
  },

  setup(props, { emit }) {
    const store = useStore();
    const router = useRouter();
    const state = reactive({
      dialogVisible: computed(() => props.open),
      form: {
        title: '',
        content: '',
      }
    });

    onMounted(() => {

    });

    const handleClose = function() {
      emit("closeStudyscheduleDialog");
    };

    return { state, handleClose };
  }
};
</script>
<style scoped>
.el-dialog__body > div {
  display: flex;
  flex-direction: column;
  align-items: center;
}
</style>
