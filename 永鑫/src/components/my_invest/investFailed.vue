<!-- 我的理财-处理失败 by fenglei -->
<template>
  <div class="invest-failed-list">
    <mt-loadmore :top-method="loadTop" :bottom-method="loadBottom" :bottom-all-loaded="allLoaded" ref="loadMore">
      <ul>
        <li v-for="(item, index) in dataList">
          <div class="top">
            <router-link :to="{ name:'investDetail', params: { projectId: item.projectId }}">
              <div class="left">
                <span class="name">{{ item.projectName }}</span>
              </div>
              <div class="right">
                <span class="status">{{ item.statusStr }}</span>
              </div>
            </router-link>
          </div>
          <div class="middle">
            <div class="item">
              <span class="text">投资金额(元)</span>
              <span class="value">{{ item.amount | currency('',2) }}</span>
            </div>
            <div class="item">
              <span class="text">投资时间</span>
              <span class="value">{{ item.createTime | dateFormatFun(4) }}</span>
            </div>
          </div>
        </li>
      </ul>
    </mt-loadmore>
    <div v-show="noData" class="no-data">
      <img src="../../assets/images/public/default/default_icon_no_hb.png">
      <p>暂无记录</p>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../ajax.config.js';

  export default {
    data() {
      return {
        dataList: [],
        allLoaded: false,
        noData: false,
        params: {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid,
          'page.page': 1,
          'page.pageSize': 10
        }
      };
    },
    created() {
      this.dataLoad();
    },
    methods: {
      dataLoad(type) {
        this.$http.get(ajaxUrl.getInvestFailed, { params: this.params }).then((res) => {
          this.noData = false;
          if (res.data.resData) {
            if (res.data.resData.list.length <= 0) {
              this.noData = true;
              return false;
            }
            if (res.data.resData.page > res.data.resData.totalPage && type == 'loadMore') {
              this.$toast('无更多数据加载哦~');
              this.allLoaded = true;
            } else {
              if (res.data.resData.totalPage == 1) {
                this.allLoaded = true;
              } else {
                this.allLoaded = false;
              }
              this.dataList = this.dataList.concat(res.data.resData.list);
            }
          }
        })
      },
      resetData() {
        this.dataList = [];
        this.params['page.page'] = 1;
      },
      loadTop() {
        setTimeout(() => {
          this.resetData();
          this.$refs.loadMore.onTopLoaded();
          this.dataLoad('reload');
        }, 1000);
      },
      loadBottom() {
        var t = setTimeout(() => {
          this.params['page.page']++;
          this.$refs.loadMore.onBottomLoaded();
          this.dataLoad('loadMore');
        }, 500);
      }
    }
  }
</script>

<style lang="sass" rel="stylesheet/sass" scoped>
  @import "investFailed.sass";
</style>
