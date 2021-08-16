<template>
  <el-dialog
    custom-class="studyschedule-dialog"
    title="스터디 일정 추가"
    v-model="state.dialogVisible"
    @close="handleClose"
  >
    <div>
      <v-date-picker v-model="state.date" style="width:90%" :color="state.pickerColor" :model-config="state.modelConfig" />
      <el-form ref="scheduleForm" :model="state.form" :rules="state.rules" label-width="120px" style="margin-top:25px; width:90%;">
        <el-form-item>
          <el-radio-group v-model="state.radio">
            <el-radio :label="1">Red</el-radio>
            <el-radio :label="2">Orange</el-radio>
            <el-radio :label="3">Green</el-radio>
            <el-radio :label="4">Indigo</el-radio>
            <el-radio :label="5">Purple</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item prop="date" label="스터디 날짜" >
          <el-input v-model="state.selectDate" placeholder="달력에서 날짜를 선택해주세요."></el-input>
        </el-form-item>
        <el-form-item label="스터디 날짜 확인" >
          <el-input v-model="state.date" placeholder="달력에서 날짜를 선택해주세요."></el-input>
        </el-form-item>
        <el-form-item prop="title" label="스터디 내용">
          <el-input v-model="state.form.title"></el-input>
        </el-form-item>
        <el-form-item style="float:right;">
          <el-button type="success" @click.once="onClickCreateSchedule">추가</el-button>
        </el-form-item>
      </el-form>
    </div>
  </el-dialog>
</template>
<script>
import { reactive, computed, ref, onMounted, watch } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";

export default {
  name: "studyschedule-dialog",

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
    const scheduleForm = ref(null);
    const state = reactive({
      dialogVisible: computed(() => props.open),
      modelConfig: {
        timeAdjust: '12:00:00',
      },
      radio: 1,
      form: {
        date: '',
        color: '',
        title: '',
      },
      pickerColor: 'red',
      date: '',
      selectDate: '',
      rules: {
        date: [
          { required: true, message: "필수 입력 항목입니다", trigger: "blur" },
          {
            validator(rule, value) {
              var error = [];
              if (value.length !== 10) {
                error = ["2021-01-01과 같이 입력해주세요."];
              }
              return error;
            }
          }
        ],
        title: [
          { required: true, message: "필수 입력 항목입니다.", trigger: "blur" },
          {
            validator(rule, value) {
              var error = [];
              if (value.length < 1) {
                error = ["최소 한글자를 입력해야 합니다."];
              } else if (value.length > 100) {
                error = ["최대 100 글자까지 입력가능합니다."];
              }
              return error;
            }
          }
        ]
      },
    });

    // radio에 따라서 color값 넣어주기
    watch(
      () => state.radio,
      () => {
       if (state.radio === 1) {
         state.pickerColor = 'red'
       }
       else if (state.radio === 2) {
         state.pickerColor = 'orange'
       }
       else if (state.radio === 3) {
        state.pickerColor = 'green'
       }
       else if (state.radio === 4) {
         state.pickerColor = 'indigo'
       }
       else {
         state.pickerColor = 'purple'
       }
       state.form.color = state.pickerColor
      }
    );

    // 영어로 들어오는 데이터 숫자로 바꿔서 보여주기
    watch(
      () => state.date,
      () => {
        let stringDate = ''
        let selectMonth = ''
        // 선택한 날짜 slice
        if (state.date) {
          stringDate = state.date.toString()
          // 영어로 나오는 월 -> 숫자로
          if (stringDate.slice(4, 7) === 'Jan') {
            selectMonth = '01'
          }
          else if (stringDate.slice(4, 7) === 'Feb') {
            selectMonth = '02'
          }
          else if (stringDate.slice(4, 7) === 'Mar') {
            selectMonth = '03'
          }
          else if (stringDate.slice(4, 7) === 'Apr') {
            selectMonth = '04'
          }
          else if (stringDate.slice(4, 7) === 'May') {
            selectMonth = '05'
          }
          else if (stringDate.slice(4, 7) === 'Jun') {
            selectMonth = '06'
          }
          else if (stringDate.slice(4, 7) === 'Jul') {
            selectMonth = '07'
          }
          else if (stringDate.slice(4, 7) === 'Aug') {
            selectMonth = '08'
          }
          else if (stringDate.slice(4, 7) === 'Sep') {
            selectMonth = '09'
          }
          else if (stringDate.slice(4, 7) === 'Oct') {
            selectMonth = '10'
          }
          else if (stringDate.slice(4, 7) === 'Nov') {
            selectMonth = '11'
          }
          else {
            selectMonth = '12'
          }
          state.selectDate = stringDate.slice(11, 15) + '-' + selectMonth + '-' + stringDate.slice(8, 10)
          state.form.date = state.selectDate
        }
        else {
          stringDate = ''
          selectMonth = ''
          state.selectDate = ''
          state.form.date = state.selectDate
        }
      }
    );

    onMounted(() => {

    });

    // 스케줄 생성하기
    const onClickCreateSchedule = function() {
      scheduleForm.value.validate(valid => {
        if (valid) {
          console.log("submit");
          store
            .dispatch("root/requestCreateSchedule", {
              date: state.date,
              color: state.pickerColor,
              title: state.form.title,
            })
              .then(function(res) {
                ElMessage({
                message: "스케줄 생성 성공!!",
                type: "success"
                });
                handleClose();
                router.push({
                  name: 'studyschedule'
                })
              })
              .catch(function(err) {
                console.log('스케줄 생성 실패ㅠㅠㅠ', err)
              })
        }
        else {
          alert('Validate Error!!')
        }
      })
    }

    const handleClose = function() {
      emit("closeStudyscheduleDialog");
    };

    return { state, scheduleForm, onClickCreateSchedule, handleClose };
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
