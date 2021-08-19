<template>
  <span v-if="streamManager">
    <ov-video :stream-manager="streamManager" :is-speak="isSpeak" />
    <div class="font-shadow">
      <p>{{ clientData }}</p>
    </div>
  </span>
</template>
<style>
.font-shadow {
  -webkit-text-stroke-width: 1px;
  -webkit-text-stroke-color: rgb(0, 0, 0);
  font-size: 80%;
}
</style>
<script>
import OvVideo from "./OvVideo";

export default {
  name: "UserVideo",

  components: {
    OvVideo
  },

  props: {
    streamManager: Object,
    isSpeak: Boolean
  },

  computed: {
    clientData() {
      const { clientData } = this.getConnectionData();
      return clientData;
    }
  },

  methods: {
    getConnectionData() {
      const { connection } = this.streamManager.stream;
      return JSON.parse(connection.data);
    }
  }
};
</script>
