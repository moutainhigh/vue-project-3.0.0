<template>
  <div>
  <div class="page cash" id="cash">
    <mt-header class="bar-nav" title="提现">
      <mt-button slot="left" icon="back" @click.native="backUrl('/account')"></mt-button>
      <router-link to="/account/cash_record/?type=tab-con2" slot="right">
        <mt-button >记录</mt-button>
      </router-link>
    </mt-header>
    <form :action="submitUrl" method="post" id="cashForm">
      <div v-if="canDisable" class="cash-top margin-t-15" @click="showBankListFun">
        <div class="bank-img">
          <img :src="defaultBankCard.picPath" >
        </div>
        <div class="bank-info" id="bank_info">
          <p class="margin-t-10">{{defaultBankCard.bankName}}</p>
          <p class=" bank-no">尾号{{defaultBankCard.hideBankNo}}　{{defaultBankCard.bankCardType}}</p>
        </div>
        <img src="../../assets/images/public/arrow_right.png" class="cash-arrow-right">
        <input type="hidden" name="bankCode" :value="bankCode">
        <input type="hidden" name="bankNo" :value="bankNo">
      </div>
      <div v-else class="cash-top margin-t-15" @click="showBankListFun">
        <div class="bank-img"><img :src="firstBank.picPath" ></div>
        <div class="bank-info" id="bank_info">
          <p class="margin-tb-10">{{firstBank.bankName}}</p>
          <p class="bank-no">尾号{{firstBank.hideBankNo}}　{{firstBank.bankCardType}}</p>
        </div>
        <img src="../../assets/images/public/arrow_right.png" class="cash-arrow-right">
        <input type="hidden" name="bankCode" :value="bankCode">
        <input type="hidden" name="bankNo" :value="bankNo">
      </div>
    <div class="cash-limit" >
      <div class="use-cash-money">
        可提现金额 <i v-if="resdata !=''" class="main-color font-arial">{{canCashMoney | currency('',2)}}</i> 元
      </div>
      <i class="all-cash" @click="allWithdraw">全部提现</i>
    </div>
    <div class="">
      <rd-money label="提现金额(元)" maxlength="8" type="text" name="amount" v-model.trim="inputMoney" placeholder="请输入金额" :readonly="resdata==''"></rd-money>
    </div>
      <input type="hidden" name="cashToken" :value="resdata.cashToken">
      <input type="hidden" name="__sid" :value="getParams.__sid">
      <input type="hidden" name="userId" :value="getParams.userId">
    </form>
    <div class="confirm-btn">
      <mt-button v-show="!inputMoney" type="default" size="large" disabled>提 现</mt-button>
      <mt-button v-show="inputMoney" @click="submitAjax" type="danger" size="large" :disabled="resdata==''">提 现</mt-button>
    </div>
    <div class="form-prompt">
      <p class="prompt-title margin-b-10">温馨提示：</p>
      <p v-html="resdata.warmTips"></p>
      <!--<p>3、预计到账时间：T日10:00AM之前申请提现，预计当日到账；T日10:00AM以后申请提现，预计最晚T+1个工作日到账；遇双休日或法定节假日顺延，实际到账时间可能会受各银行工作时间影响。</p>-->
    </div>
  </div>
    <!--银行卡列表-->
  <transition
    name="custom-classes-transition"
    enter-active-class="animated slideInRight"
    leave-active-class="animated slideOutRight"
  >
  <div v-show="showBankList" class="page bank-show" id="bank_show">
    <mt-header class="bar-nav" title="选择银行卡">
      <mt-button slot="left" icon="back" @click.native="hideBankListFun"></mt-button>
    </mt-header>
    <div v-for="(item,index) in resdata.bankList" :class="{'selected': !item.canDisable}" class="cash-top margin-t-15 aui-border-b" @click="selectBank('b'+index)" >
      <div class="bank-img">
        <img :src="item.picPath" >
      </div>
      <div class="bank-info" :ref="'b'+index" :data-code="item.bankCode" :data-no="item.bankNo">
        <p class="margin-tb-10">{{item.bankName}}</p>
        <p class="bank-no">尾号{{item.hideBankNo}}　{{item.bankCardType}}</p>
      </div>
    </div>
    <!--<div class="cash-top margin-t-15 aui-border-b" @click="selectBank('b2')">-->
      <!--<div class="bank-img">-->
        <!--<img src="../../assets/images/public/bank_logo/withdraw/withdraw_icon_ABOC.png" >-->
      <!--</div>-->
      <!--<div class="bank-info" ref="b2" data-code="21" data-no="22">-->
        <!--<p class="margin-tb-10">中国农业银行</p>-->
        <!--<p class="bank-no">尾号6666　储蓄卡</p>-->
      <!--</div>-->
    <!--</div>-->
  </div>
  </transition>
  </div>
</template>
<script>
  import * as ajaxUrl from '../../ajax.config'
  import backUrl from '../../mixins/backUrl'
  import RdMoney from '../../components/MoneyInput.vue'
  export default {
    name: 'withdraw',
    created(){
      this.$indicator.open({spinnerType: 'fading-circle'}) //提示初始化加载
      this.$http.get(ajaxUrl.toCash, {params: this.getParams}).then((res) => {
        if(res.data.resData == '') return;
        var bankList = res.data.resData.bankList
        for(var i = 0; i < bankList.length; i++){
          if(!bankList[i].canDisable){
            this.canDisable = true;
            this.defaultBankCard = bankList[i]
            this.bankCode = bankList[i].bankCode
            this.bankNo = bankList[i].bankNo
            this.curSelectRef = 'b' + i
          } else {
            this.bankCode = bankList[0].bankCode
            this.bankNo = bankList[0].bankNo
          }
        }
        this.resdata = res.data.resData
        this.firstBank = this.resdata.bankList[0]
        this.canCashMoney = this.resdata.cashMoney < this.resdata.userMoney ? this.resdata.cashMoney : this.resdata.userMoney
        this.resdata.warmTips = res.data.resData.warmTips.replace(/\n/g, '<br/>')
        this.$indicator.close()
      })
    },
    components:{RdMoney},
    mixins: [backUrl],
    methods: {
      showBankListFun(){
        this.showBankList = true;
        // 进入银行列表标记已✔️中的列表
        if(!this.curSelectRef) return // 为空时返回不执行
        this.$refs[this.curSelectRef][0].parentElement.className = 'cash-top selected margin-t-15 aui-border-b'
      },
      hideBankListFun(){this.showBankList = false;},
      selectBank(ref){
        let selectNode = this.$refs[ref][0]
        let bank_code = selectNode.dataset.code
        let bank_no = selectNode.dataset.no
        let bank_pic = selectNode.previousElementSibling.children[0].src
        let bankInfo = document.getElementById('bank_info')
        bankInfo.previousElementSibling.children[0].src = bank_pic
        bankInfo.children[0].innerText = selectNode.children[0].innerText
        bankInfo.children[1].innerText = selectNode.children[1].innerText
        // 给选择的银行卡加✔️
        let card_list = document.getElementsByClassName('cash-top')
        for(let i = 0; i < card_list.length; i++){
          card_list[i].className = 'cash-top margin-t-15 aui-border-b'
        }
        // 给当前点击的父节点加✔️
        selectNode.parentElement.className = 'cash-top selected margin-t-15 aui-border-b'
        this.curSelectRef = ref; // 把当前点击选择的ref赋值给data作为下次进来的标记
        //选择时传递银行的bankCode 和 bankNo
        this.bankCode = bank_code
        this.bankNo = bank_no
        this.showBankList = false
      },
      allWithdraw () { // 点击全部提现
        if(this.canCashMoney > this.resdata.cashSingleMaxAmountLimit){ // 可提现金额大于单笔可提现额度
          this.inputMoney = this.resdata.cashSingleMaxAmountLimit
        }else{
          this.inputMoney = this.canCashMoney
        }
      },
      submitAjax(){
        if(this.resdata.todayDoCashTimes >= this.resdata.cashSingleDayTimeLimit){ //
          this.$toast('今日提现次数已达上限')
          return
        }
        if(this.inputMoney == 0){ //
          this.$toast('请输入提现金额大于零的数字')
          return
        }
        if(this.inputMoney < this.resdata.cashMinAmount){ //
          this.$toast('最小提现金额为' + this.resdata.cashMinAmount +'元')
          return
        }
        if(this.inputMoney > this.resdata.cashSingleMaxAmountLimit){ //
          this.$toast('单笔提现不能超过' + this.resdata.cashSingleMaxAmountLimit +'元')
          return
        }
        if(this.inputMoney > this.resdata.cashMoney){ //
          this.$toast('您今日还可提现' + this.resdata.cashMoney +'元')
          return
        }
        if(this.inputMoney > this.resdata.userMoney){ //
          this.$toast('提现金额不能大于您的可用余额')
          return
        }
        if(this.repeatFlag){
          this.repeatFlag = false  // 防重复提交
          document.getElementById('cashForm').submit()
        }

      }
    },
    watch: {
      inputMoney (newV, oldV) {
        if( newV.toString().search(/^([1-9]\d*|0)/) == -1){ //解决刚开始输入不符合规则的bug
          this.inputMoney = ''
        }
      }
    },
    data(){
      return {
        submitUrl: ajaxUrl.MobileServer + '/app/member/cash/doCash.html',
        curSelectRef: '',
        showBankList: false,
        resdata: '',
        firstBank: '',
        bankNo:'',
        bankCode: '',
        canDisable: false,
        repeatFlag: true,
        defaultBankCard: '',
        inputMoney:'',
        canCashMoney: 0,
        getParams: {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        }
      }
    }
  }
</script>
<style lang="scss" rel="styleSheet/scss" scoped>
  @import "../../assets/css/animate.min.css";
  @import "../../assets/scss/var.scss";
  .confirm-btn{  margin: .3rem 5%;  }
  .cash-top{
    height: .6rem;
    width: 100%;
    padding: 0 5%;
    background:#fff;
  }
  .cash-top.selected {
    background:#fff url('../../assets/images/money/card_selected.png') 90% center no-repeat;
    background-size: .16rem;
  }
  .bank-img{
    float: left;
    width: .55rem;
    height: 100%;
  }
  .bank-img img{
    height: .38rem;
    width: .38rem;
    vertical-align: middle;
    margin-top: .11rem;
  }
  .bank-info{
    width: 60%;
    float: left;
    height: 100%;
  }
  .bank-info p{
    font-size: .14rem;
  }
  .bank-no{
    color: #999;
    line-height: 1;
    margin-top: .06rem;
  }
  .cash-arrow-right{
    height: .14rem;
    float: right;
    margin-top: .23rem;
  }
  .cash-limit{
    height: .48rem;
    width: 100%;
    padding: 0 5%;
  }
  .use-cash-money{
    width: 50%;
    height: .48rem;
    line-height: .48rem;
    float: left;
    font-size: .12rem;
    color: #999;
  }
  .all-cash{
    height: .24rem;
    line-height: .22rem;
    padding: 0 .1rem;
    font-size: .13rem;
    display: inline-block;
    float: right;
    margin-top: .12rem;
    color: $main-color;
    border: 1px solid $main-color;
    border-radius: .12rem;
  }
</style>
