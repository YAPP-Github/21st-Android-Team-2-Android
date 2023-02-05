# Find Things Architecture

## 목차
- [MVVM + UDA](#mvvm--uda)
- [아키텍쳐, 바인딩 등에 대한 구조적 설명(MVVM, Model 상속받아서 처리하는 방식, JSON구조, DataBindHelper 구현)](#%EC%95%84%ED%82%A4%ED%85%8D%EC%B3%90-%EB%B0%94%EC%9D%B8%EB%94%A9-%EB%93%B1%EC%97%90-%EB%8C%80%ED%95%9C-%EA%B5%AC%EC%A1%B0%EC%A0%81-%EC%84%A4%EB%AA%85mvvm-model-%EC%83%81%EC%86%8D%EB%B0%9B%EC%95%84%EC%84%9C-%EC%B2%98%EB%A6%AC%ED%95%98%EB%8A%94-%EB%B0%A9%EC%8B%9D-json%EA%B5%AC%EC%A1%B0-databindhelper-%EA%B5%AC%ED%98%84)
  - [아키텍쳐 - MV(VC)VM(Action in Model)](#아키텍쳐---mvvcvmaction-in-model)
  - [서버에서 Response받는 JSON 구조 - ex) 유저 리스트 Response](#서버에서-response받는-json-구조---ex-유저-리스트-response)
  - [데이터 클래스 - Model](#데이터-클래스---model)
  - [Data를 상속받은 자식 클래스 - ex) User.kt](#data를-상속받은-자식-클래스---ex-userkt)
  - [BaseViewModel](#baseviewmodel)
  - [BaseStateViewModel](#basestateviewmodel)
  - [뷰 모델 - ex) LikeTabViewModel](#뷰-모델---ex-liketabviewmodel)
  - [VM에서 구독한 데이터를 받아 뷰에 뿌려주는 VC - ex) LikeTabFragment](#vm에서-구독한-데이터를-받아-뷰에-뿌려주는-vc---ex-liketabfragment)
  - [Data의 액션핸들러의 기능을 정의해주는 헬퍼 - DataBindHelper](#data의-액션핸들러의-기능을-정의해주는-헬퍼---databindhelper)
  - [DataListAdapter를 이용한 type별 셀 분류](#datalistadapter를-이용한-type별-셀-분류)
  - [각 타입에 맞는 인스턴스를 매핑 - DataMapper](#각-타입에-맞는-인스턴스를-매핑---datamapper)
  - [각 레이아웃에 맞는 뷰 홀더 매핑 - DataViewHolderMapper](#각-레이아웃에-맞는-뷰-홀더-매핑---dataviewholdermapper)
  - [뷰 홀더 구현 - ex) UserViewHolder.kt](#뷰-홀더-구현---ex-userviewholderkt)
  - [데이터에 대한 동적인 갱신](#데이터에-대한-동적인-갱신)

## MVVM + UDA
<img width="653" alt="App Architecture" src="https://user-images.githubusercontent.com/8112952/216831834-a53da45d-899c-42b3-a1e9-1e2d6f82bf16.png">

---

## 아키텍쳐, 바인딩 등에 대한 구조적 설명(MVVM, Model 상속받아서 처리하는 방식, JSON구조, DataBindHelper 구현)

### 아키텍쳐 - MV(VC)VM(Action in Model)

> Jetpack에서 제공하는 AAC-ViewModel, 코루틴을 이용하여 MVVM + UDA 아키텍처를 구현하였습니다. 그래서 DataBinding 라이브러리를 사용하지 않으며, DataBindHelper를 이용하여 리스트의 있는 모델에 액션을 정의하여 데이터 바인딩하는 구조로 이루어져 있습니다. 데이터 바인딩을 이용하지 않는 이유는 추후 로직에 대한 고도화가 이루어 졌을 때 iOS와 안드로이드 아키텍처를 동일하게 가져감으로써 유지보수성을 높이고 어떤 개발자가 와도 개발하는데 있어 원활하게 하기 위함입니다.(물론, 아키텍쳐를 반드시 동일하게 가져갈 필요는 없습니다. 이것은 처해진 상황에 따라 다른 것이라 생각합니다.)

### 서버에서 Response받는 JSON 구조 - ex) 유저 리스트 Response

```
{
  data : [
    {
      "type": "user.cell",
      "id": 100,
      "scheme": "dncapp://users/100",
      ...
    },
  ],
  "meta": {
      "pagenation": {...},
      ...
  }
}
```

- 아키텍쳐 및 데이터 구조에 영감을 준 문서 - [https://jsonapi.org](https://jsonapi.org/)

위 예시는 서버에서 받은 Json Element 중 type를 통해 리스트 Cell을 구조화 하여 받고 있습니다.각 모델 타입의 뒤에 cell이 오는지, detail 등 어떤 타입이 오는지에 따라 리스트화면인지, 상세화면인지 보여지는 뷰와 매핑하는 작업을 거치게 됩니다.

### 데이터 클래스 - Model

```kotlin
open class Data(
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "data_id") open var id: Long = 0,
  @ColumnInfo(name = "data_type") open var type: String = CellType.EMPTY_CELL.type,
  @Ignore open var scheme: String = "null"
) : Parcelable
```

모든 데이터 클래스의 기본이 되는 Data 클래스입니다. 기본적으로 서버에서 내려주는 프로퍼티로 id, type, scheme이 있으며, DataBindHelper를 통해 데이터에 대한 핸들러 구현을 해주게 됩니다.

```kotlin
var handler: DataHandler = { }
var detailHandler: DataHandler = { }
var deleteHandler: DataHandlera = { }
```

해당 모델의 액션에 대한 동작은 run...() 함수를 통해 동작하는 방식으로 구현되어 있습니다.

### Data를 상속받은 자식 클래스 - ex) User.kt

```kotlin
data class User(
  @ColumnInfo var login: String,
  @ColumnInfo var number: Int,
  @ColumnInfo var avatarUrl: String
) : Data() {
  ...
  var isFavorite: Boolean = false
  ...
  var favoriteVariable: Variable<Boolean>? = null
  ...
  fun setIsFavorite(isFavorite: Boolean) {
    this.isFavorite = isFavorite
    favoriteVariable?.set(isFavorite)
  }
  ...
  var favoriteHandler: DataHandler = { }

  fun runFavorite() = favoriteHandler.invoke(this)
}
```

위 예시의 경우 유저 리스트 표시에 필요한 데이터 클래스 User입니다. Data를 상속받고 필요한 프로퍼티 및 동작에 대해 정의할 수 있으며, 동적인 데이터 갱신을 위해 안드로이드 Jetpack에서 제공하는 LiveData 대신 Variable 클래스를 구현하여 동적으로 데이터를 갱신해주고 있습니다.

제가 LiveData를 사용하지 않고 Variable를 만든 이유에 대해서는 제 블로그에 올려두었습니다.

[RxJava로 LiveData 따라해보기](https://soda1127.github.io/reactive-variable-like-livedata/)

### BaseViewModel

```kotlin
abstract class BaseViewModel: ViewModel() {

    protected val jobs = mutableListOf<Job>()

    open fun fetchData(): Job = viewModelScope.launch {  }

    override fun onCleared() {
        jobs.forEach {
            if (it.isCancelled.not())
                it.cancel()
        }
        super.onCleared()
    }

}
```

기본적으로 모든 프로젝트에 구현되는 ViewModel은 모두 BaseViewModel을 상속 받습니다. 모든 화면은 기본적으로 data를 fetch한다는 판단하에 open function을 구현하게 되었습니다.

이를 구독하는 화면은 BaseFragment, BaseActivity가 있습니다. 자세한 것은 코드를 참고 부탁드립니다.

### BaseStateViewModel

```kotlin
abstract class BaseStateViewModel<S : State, SE : SideEffect> : BaseViewModel() {

    protected abstract val _stateFlow: MutableStateFlow<S>
    val stateFlow: StateFlow<S>
        get() = _stateFlow

    protected abstract val _sideEffectFlow: MutableSharedFlow<SE>
    val sideEffectFlow: SharedFlow<SE>
        get() = _sideEffectFlow

    protected inline fun <reified S : State> withState(accessState: (S) -> Unit): Boolean {
        if (stateFlow.value is S) {
            accessState(stateFlow.value as S)
            return true
        }
        return false
    }

    protected fun setState(state: S) {
        _stateFlow.value = state
    }

    protected fun postSideEffect(sideEffect: SE) = viewModelScope.launch {
        _sideEffectFlow.emit(sideEffect)
    }

}
```

BaseStateViewModel은 State와 SideEffect가 필요한 경우 사용됩니다. 상속받는 클래스에서는 기본적으로 제너릭으로 State와 SideEffect를 구현하는 구현체 클래스의 타입을 명시하여 사용합니다.

또한, Activity 및 Fragment에서 flow 스트림에 대한 구독이 필요한 경우, stateFlow 변수 및 sideEffectFlow에 접근하여 사용하도록 합니다.

- `fun withState((S) -> Unit): Boolean` : StateFlow의 현재 State 타입 및 해당하는 상태인 경우 값에 access하도록 합니다. 람다 인자는 immutable하며, 외부 람다 블록에서 접근할 수 없습니다.

- `fun setState(state: S)` : stateFlow의 현재 값을 갱신합니다. StateFlow에는 Screen의 State를 처리할 수 있는 상태들이 포함됩니다.

- **ex) LikeTabState.kt**

  ```kotlin
  sealed class LikeTabState: State {
  
      object Uninitialized : LikeTabState()
  
      object Loading : LikeTabState()
  
      data class Success(
          val dataList: List<Data>
      ) : LikeTabState()
  
      data class Error(
          val e: Throwable
      ) : LikeTabState()
  }
  ```

- `fun postSideEffect(sideEffect: SE): Job` : sideEffect의 값을 갱신합니다. 이 때, SideEffect는 Screen에 포함되는 상태가 아닌 Fire & Forget되는 One-Shot Event가 해당됩니다.

  - ShowToast, ShowDialog, StartActivity 등…

- **ex) LikeTabSideEffect.kt**

  ```kotlin
  sealed class LikeTabSideEffect: SideEffect {
  
      data class ShowToast(val message: String): LikeTabSideEffect()
  
  }
  ```

### 뷰 모델 - ex) LikeTabViewModel

```kotlin
class LikeTabViewModel : BaseStateViewModel<LikeTabState, LikeTabSideEffect>() {

    override val _stateFlow: MutableStateFlow<LikeTabState>
        get() = MutableStateFlow(LikeTabState.Uninitialized)
    override val _sideEffectFlow: MutableSharedFlow<LikeTabSideEffect>
        get() = MutableSharedFlow()

    override fun fetchData(): Job = viewModelScope.launch {
        setState(LikeTabState.Loading)
        setState(
            LikeTabState.Success(
                (0..5).map {
                    LikeItem(
                        id = it.toLong(),
                        name = "즐겨찾기 아이템 ${it}번"
                    )
                }
            )
        )
    ...
    }

    ...

}
```

예시로 든 LikeTabViewModel은 BaseStateViewModel을 상속받아 사용하며, 모든 모델을 리스트에 담아 가공 및 방출하는 기능을 수행합니다.

이 때, 상태관리를 위해 LikeTabState, LikeTabSideEffect을 제너릭에 명시하며, 이에 따라 각 전역 변수를 오버라이딩 해줍니다.

이를 상속받는 리스트 화면을 위한 뷰 모델에서 `fetchData(): Job` 함수로 네트워크에서 받아온 respnse body를 가공해 통해 데이터 리스트를 받은 후  상태를 다음과 같이 갱신 해 줍니다.

### VM에서 구독한 데이터를 받아 뷰에 뿌려주는 VC - ex) LikeTabFragment

```kotlin
@AndroidEntryPoint
class LikeTabFragment : BaseStateFragment<LikeTabViewModel, FragmentLikeTabBinding>() {
  ...
  override fun observeData(): Job {
        val job = viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    vm.stateFlow.collect { state ->
                        when (state) {
                            is LikeTabState.Uninitialized -> Unit
                            is LikeTabState.Loading -> handleLoading(state)
                            is LikeTabState.Success -> handleSuccess(state)
                            is LikeTabState.Error -> handleError(state)
                        }
                    }
                }
                launch {
                    vm.sideEffectFlow.collect { sideEffect ->
                        when (sideEffect) {
                            is LikeTabSideEffect.ShowToast -> {
                                Toast.makeText(requireContext(), sideEffect.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
        return job
    }
}
```

이를 구독하고 있는 LikeTabFragment에서 구독중인 데이터를 받게되면, 데이터를 받아 DataBindHelper에 모든 모델 인스턴스에 필요한 핸들러에 대한 정의를 합니다.데이터에 대한 액션 반영이 끝나면, 어댑터에 반영합니다.

```kotlin
@AndroidEntryPoint
class LikeTabFragment : BaseStateFragment<LikeTabViewModel, FragmentLikeTabBinding>() {

    ...
    @Inject
    lateinit var dataBindHelper: DataBindHelper

    ...

    private fun handleSuccess(likeTabState: LikeTabState.Success) {
	dataBindHelper.bindList(likeTabState.dataList, vm)
	dataListAdapter?.submitList(likeTabState.dataList)
    }

    ...

}
```

### Data의 액션핸들러의 기능을 정의해주는 헬퍼 - DataBindHelper

```kotlin
@Singleton
class DataBindHelper @Inject constructor(
    @HomeLikeItemQualifier //각 모듈에 동일 Data에 대한 핸들러 대응이 필요하다면 아래와 같이 추가
    private val homeLikeItemBinder: LikeItemBinder,
    @CategoryLikeItemQualifier //각 모듈에 동일 Data에 대한 핸들러 대응이 필요하다면 아래와 같이 추가
    private val categoryLikeItemBinder: LikeItemBinder,
) {

    @SuppressLint("CheckResult")
    fun bindList(dataList: List<Data>, viewModel: BaseViewModel) {
        dataList.forEach { data ->
            bindData(data, viewModel)
        }
    }

    private fun bindData(data: Data, viewModel: BaseViewModel) {
        when(data.type) {
            CellType.LIKE_CELL -> {
                homeLikeItemBinder.bindData(data as LikeItem, viewModel)
								//categoryLikeItemBinder.bindData(data as LikeItem, viewModel)
            }
            else -> { }
        }
    }

}
```

이를 구독하고 있는 화면에서에서 DataBindHelper에 모든 모델 인스턴스에 필요한 핸들러에 대한 정의를 합니다.

핸들러 처리는 타입 및 ViewModel을 어떤 것을 들고 있냐에 따라 처리를 다르게 할 수 있습니다.

각 모듈에 동일 Data에 대한 핸들러 대응이 필요하다면 :feature:common 모듈에서는 interface로 다음과 같이 정의하고, 구현체는 각 :feature:{feature} 모듈에 정의합니다.

```kotlin
@Singleton
class HomeLikeItemBinder @Inject constructor(): LikeItemBinder {

    override fun bindData(data: LikeItem, viewModel: BaseViewModel) {
        when (viewModel) {
            is LikeTabViewModel -> setLikeTabViewModelHandler(data, viewModel)
            is HomeTabViewModel -> setHomeTabViewModelHandler(data, viewModel)
        }
    }

    private fun setLikeTabViewModelHandler(item: LikeItem, viewModel: LikeTabViewModel) {
        item.deleteHandler = { data ->
            viewModel.deleteItem(data as LikeItem)
        }
        item.updateHandler = { data ->
            viewModel.updateCount(data as LikeItem)
        }
    }

    ...
}
```

이에 대한 interface를 업캐스팅 하는 DI는 다음과 같이 `{feature_name}DataBinderModule` 로 이름을 정의하여 패키지에 구햔합니다.

```kotlin
package com.yapp.itemfinder.home.binder.di

...

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeDataBinderModule {

    @Binds
    @Singleton
    @HomeLikeItemQualifier
    abstract fun bindLikeItemBinder(
        homeLikeItemBinder: HomeLikeItemBinder
    ): LikeItemBinder

}
```

### DataListAdapter를 이용한 type별 셀 분류

```kotlin
class DataListAdapter<D : Data> : ListAdapter<D, DataViewHolder<D>>(
    object : DiffUtil.ItemCallback<D>() {
        override fun areItemsTheSame(oldItem: D, newItem: D): Boolean =
            oldItem.id == newItem.id && oldItem.type == newItem.type

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: D, newItem: D): Boolean =
            oldItem === newItem
    }
) {

    override fun getItemViewType(position: Int) = getItem(position).type.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder<D> {
        return DataViewHolderMapper.map(parent, CellType.values()[viewType])
    }

    override fun onBindViewHolder(holder: DataViewHolder<D>, position: Int) {
        val safePosition = holder.adapterPosition
        if (safePosition != RecyclerView.NO_POSITION) {
            @Suppress("UNCHECKED_CAST")
            val model = getItem(position) as D
            with(holder) {
                bindData(model)
                bindViews(model)
            }
        }
    }
}
```

- DiffUtil을 통해 Data 클래스를 상속받은 UI Model에 대한 처리를 진행합니다.

```kotlin
dataListAdapter?.submitList(homeTabState.dataList)
```

- Adapter를 생성 후, 화면에서 adapter의 submitList(dataList: List<Data>)를 호출합니다.

```kotlin
override fun getItemViewType(position: Int)DataLayoutMapper
```

- 매퍼를 통해 각 인스턴스의 type값과 비교하여 맞는 layoutId값을 반환합니다.

```kotlin
override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder<T>
```

- viewType Id를 받아내어 해당 layoutId에 맞는 ViewHolder 인스턴스를 반환하게 됩니다.

```kotlin
override fun onBindViewHolder(holder: DataViewHolder<T>, position: Int)
```

- DataViewHolder 는 추상클래스이며, 하위 메서드를 갖고 있습니다.

```kotlin
abstract fun reset() // VH에서 들고 있는 뷰를 초기화 합니다.

open fun bindData(data: D) {
    reset() // 데이터를 뷰에 바인딩합니다.
} 

abstract fun observeData(data: T) // 반영이 될 수 있는 데이터를 Data내 Vaiable을 통해 구독해 반영합니다.

abstract fun bindViews(data: T) // 뷰에 리스너를 달고, 액션이 일어나는 경우 Data의 핸들러를 트리거합니다.
```

### 각 타입에 맞는 인스턴스를 매핑 - DataMapper

- 서버에서 내려주는 응답에 type이 들어오면, 이를 적합한 데이터 타입으로 컨버트 해줍니다.
- 

```kotlin
@Singleton
class DataMapper @Inject constructor(
    @ApiGsonQualifier
    private val apiGson: Gson,
) {

    fun map(json: JsonObject): Data? =
        when (json.get("type").asString) {
            CellType.EMPTY_CELL.name -> convertJsonType(json, Data::class)
            CellType.CATEGORY_CELL.name -> convertJsonType(json, Category::class)
            CellType.LIKE_CELL.name -> convertJsonType(json, LikeItem::class)
            else -> null
        }

    private fun convertJsonType(json: JsonObject, clazz: KClass<out Data>): Data {
        return apiGson.fromJson(json.toString(), clazz.java)
    }
}
```

보여지는 예로 LIKE_CELL을 받게되면, 리스트 셀의 모델을 지칭하며, Gson을 통해 파싱되어 Data 인스턴스로 객체화 하여 반환 되는 방식입니다.

### 각 레이아웃에 맞는 뷰 홀더 매핑 - DataViewHolderMapper

```kotlin
object DataViewHolderMapper {
		
    @Suppress("UNCHECKED_CAST")
    fun <D: Data> map(
        parent: ViewGroup,
        type: CellType,
    ): DataViewHolder<D> {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = when (type) {
            CellType.EMPTY_CELL -> null
            CellType.CATEGORY_CELL -> CategoryViewHolder(ViewholderStorageBinding.inflate(inflater,parent,false))
            CellType.LIKE_CELL -> LikeViewHolder(LikeItemBinding.inflate(inflater, parent, false))
        }

        return viewHolder as DataViewHolder<D>
    }

}
```

DataListAdapter에서 DataViewHolder.map함수를 기반으로 `onCreateViewHolder(ViewGroup, int)` 함수를 통해 layoutId를 받아 각 레이아웃에 일치하는 ViewHodler를 반환하게 됩니다.

### 뷰 홀더 구현 - ex) UserViewHolder.kt

```kotlin
class LikeViewHolder(
    val binding: LikeItemBinding
) : DataViewHolder<LikeItem>(binding) {

    override fun reset() {
        // TODO
    }

    override fun bindData(data: LikeItem) {
        super.bindData(data)
        binding.likeItemTv.text = data.name
    }

    override fun bindViews(data: LikeItem) {
        binding.likeItemTv.setOnClickListener { data.goLikeDetailPage() }
        binding.deleteBtn.setOnClickListener {
            data.deleteLikeItem()
        }
        binding.likeItemTv.setOnClickListener {
            data.updateLikeItem()
        }
    }

}
```

DataViewHolder는 공통적으로 Data을 상속받은 모든 클래스에 대해 데이터를 바인딩하는 `bindData(T : Data)` , `bindViews(T : Data)` 함수를 구현해야 합니다. 예시와 같이 LikeItem을 사용하는 뷰홀더는 DataViewHolder<LikeItem>를 상속받아 사용하며, DataBindHelper에서 정의한 모델에 대한 액션을 `LikeItem.deleteLikeItem()`, `LikeItem.updateLikeItem()` 함수를 통해 호출하여 동작할 수 있습니다.

### 데이터에 대한 동적인 갱신

비즈니스 로직 구조상 다른 화면의 데이터 리스트도 실시간으로 갱신되어야 할 필요가 있기때문에 모델 리스트를 갖고 있는 뷰 모델에서는 데이터 갱신 로직이 요구되었습니다.

이를 해결하기 위해 상태를 공통적으로 관리할 수 있는 BaseStateViewModel에서 State의 상태에 접근하여 값을 갱신할 수 있습니다.

```kotlin
class LikeTabViewModel : BaseStateViewModel<LikeTabState, LikeTabSideEffect>() {

    ...
	
    fun deleteItem(item: LikeItem): Job = viewModelScope.launch {
        val withState = withState<LikeTabState.Success> { state ->
            setState(
                state.copy(
                    dataList = state.dataList.toMutableList().apply {
                        remove(item)
                    }
                )
            )
            postSideEffect(
                LikeTabSideEffect.ShowToast(
                    "${item}이 삭제됐습니다."
                )
            )
        }
    }

    ...

}
```
