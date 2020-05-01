package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import static org.mockito.Mockito.*;

public class GT4500Test {

  private TorpedoStore primaryTs;
  private TorpedoStore secondaryTs;

  private GT4500 ship;

  @BeforeEach
  public void init(){
    primaryTs = mock(TorpedoStore.class);
    secondaryTs = mock(TorpedoStore.class);
    this.ship = new GT4500(primaryTs, secondaryTs);
  }

  // Specification tests

  @Test
  public void fireTorpedo_Single_FireOnce_PrimaryFired(){
    when(primaryTs.fire(1)).thenReturn(true);
    when(secondaryTs.fire(1)).thenReturn(true);

    boolean fireSuccess = ship.fireTorpedo(FiringMode.SINGLE);

    assertEquals(true, fireSuccess);
    verify(primaryTs, times(1)).fire(1);
    verify(secondaryTs, times(0)).fire(1);
  }

  @Test
  public void fireTorpedo_Single_FireTwice_PrimaryThenSecondaryFired(){
    when(primaryTs.fire(1)).thenReturn(true);
    when(secondaryTs.fire(1)).thenReturn(true);

    boolean fireSuccess1 = ship.fireTorpedo(FiringMode.SINGLE);
    boolean fireSuccess2 = ship.fireTorpedo(FiringMode.SINGLE);

    assertEquals(true, fireSuccess1);
    assertEquals(true, fireSuccess2);
    InOrder inOrder = inOrder(primaryTs, secondaryTs);
    inOrder.verify(primaryTs).fire(1);
    inOrder.verify(secondaryTs).fire(1);
  }

  @Test
  public void fireTorpedo_Single_PrimaryEmpty_FireOnce_SecondaryFired(){
    when(primaryTs.fire(1)).thenReturn(true);
    when(secondaryTs.fire(1)).thenReturn(true);
    when(primaryTs.isEmpty()).thenReturn(true);

    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    assertEquals(true, result);
    verify(primaryTs, times(0)).fire(1);
    verify(secondaryTs, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_Single_SecondaryEmpty_FireOnce_PrimaryFired(){
    when(primaryTs.fire(1)).thenReturn(true);
    when(secondaryTs.fire(1)).thenReturn(true);
    when(secondaryTs.isEmpty()).thenReturn(true);

    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    assertEquals(true, result);
    verify(primaryTs, times(1)).fire(1);
    verify(secondaryTs, times(0)).fire(1);
  }

  @Test
  public void fireTorpedo_All_FireOnce_BothFired(){
    when(primaryTs.fire(1)).thenReturn(true);
    when(secondaryTs.fire(1)).thenReturn(true);
    
    boolean fireSuccess = ship.fireTorpedo(FiringMode.ALL);

    assertEquals(true, fireSuccess);
    verify(primaryTs, times(1)).fire(1);
    verify(secondaryTs, times(1)).fire(1);
  }

  // Source code tests

  @Test
  public void fireTorpedo_Single_PrimaryFiredLast_SecondaryNotEmpty_SecondaryFired(){
    when(primaryTs.fire(1)).thenReturn(true);
    when(secondaryTs.fire(1)).thenReturn(true);
    when(secondaryTs.isEmpty()).thenReturn(false);
    ship.fireTorpedo(FiringMode.SINGLE);

    boolean fireSuccess = ship.fireTorpedo(FiringMode.SINGLE);

    assertEquals(true, fireSuccess);
    verify(secondaryTs, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_Single_PrimaryFiredLast_SecondaryEmptyButPrimaryNotEmpty_PrimaryFired(){
    when(primaryTs.fire(1)).thenReturn(true);
    when(secondaryTs.fire(1)).thenReturn(true);
    when(primaryTs.isEmpty()).thenReturn(false);
    when(secondaryTs.isEmpty()).thenReturn(true);
    ship.fireTorpedo(FiringMode.SINGLE);

    boolean fireSuccess = ship.fireTorpedo(FiringMode.SINGLE);

    assertEquals(true, fireSuccess);
    verify(primaryTs, times(2)).fire(1);
    verify(secondaryTs, times(0)).fire(1);
  }

  @Test
  public void fireTorpedo_Single_PrimaryFiredLast_SecondaryAndPrimaryEmpty_Failed(){
    when(primaryTs.fire(1)).thenReturn(true);
    when(secondaryTs.fire(1)).thenReturn(true);
    ship.fireTorpedo(FiringMode.SINGLE);
    when(primaryTs.isEmpty()).thenReturn(true);
    when(secondaryTs.isEmpty()).thenReturn(true);

    boolean fireSuccess = ship.fireTorpedo(FiringMode.SINGLE);

    assertEquals(false, fireSuccess);
    verify(primaryTs, times(1)).fire(1);
    verify(secondaryTs, times(0)).fire(1);
  }

  @Test
  public void fireTorpedo_All_PrimaryFiredLast_SecondaryNotEmpty_SecondaryFired(){
    when(primaryTs.fire(1)).thenReturn(true);
    when(secondaryTs.fire(1)).thenReturn(true);
    when(secondaryTs.isEmpty()).thenReturn(false);
    ship.fireTorpedo(FiringMode.SINGLE);

    boolean fireSuccess = ship.fireTorpedo(FiringMode.ALL);

    assertEquals(true, fireSuccess);
    verify(secondaryTs, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_All_PrimaryFiredLast_PrimaryNotEmpty_PrimaryFired(){
    when(primaryTs.fire(1)).thenReturn(true);
    when(secondaryTs.fire(1)).thenReturn(true);
    when(primaryTs.isEmpty()).thenReturn(false);
    ship.fireTorpedo(FiringMode.SINGLE);

    boolean fireSuccess = ship.fireTorpedo(FiringMode.ALL);

    assertEquals(true, fireSuccess);
    verify(primaryTs, times(2)).fire(1);
  }

  @Test
  public void fireTorpedo_All_PrimaryFiredLast_PrimaryAndSecondaryEmpty_Failed(){
    when(primaryTs.fire(1)).thenReturn(true);
    when(secondaryTs.fire(1)).thenReturn(true);
    ship.fireTorpedo(FiringMode.SINGLE);
    when(primaryTs.isEmpty()).thenReturn(true);
    when(secondaryTs.isEmpty()).thenReturn(true);

    boolean fireSuccess = ship.fireTorpedo(FiringMode.ALL);

    assertEquals(false, fireSuccess);
    verify(primaryTs, times(1)).fire(1);
    verify(secondaryTs, times(0)).fire(1);
  }

  @Test
  public void fireTorpedo_All_NotPrimaryFiredLast_PrimaryAndSecondaryEmpty_Failed(){
    when(primaryTs.fire(1)).thenReturn(true);
    when(secondaryTs.fire(1)).thenReturn(true);
    when(primaryTs.isEmpty()).thenReturn(true);
    when(secondaryTs.isEmpty()).thenReturn(true);

    boolean fireSuccess = ship.fireTorpedo(FiringMode.ALL);

    assertEquals(false, fireSuccess);
    verify(primaryTs, times(0)).fire(1);
    verify(secondaryTs, times(0)).fire(1);
  }
}
